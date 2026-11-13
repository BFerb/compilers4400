/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved.  */
package Translate;

import Temp.Label;
import Temp.Temp;
import Symbol.Symbol;
import Symbol.Table;
import Types.CLASS;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements translation of abstract syntax expressions
 * into intermediate trees.
 */

public class Translate implements Absyn.TreeVisitor
{
    public Frame.Frame frame;
    private CLASS currentClass;

    public Translate(Frame.Frame frame) { this.frame = frame; }


    private static Tree.Exp CONST(int value)  { return new Tree.CONST(value); }
    private static Tree.Exp NAME(Label label) {	return new Tree.NAME(label); }
    private static Tree.Exp TEMP(Temp temp)   {	return new Tree.TEMP(temp); }
    private static Tree.Exp BINOP(int binop, Tree.Exp left, Tree.Exp right)
    {   return new Tree.BINOP(binop, left, right);   }
    private static Tree.Exp MEM(Tree.Exp exp) {	return new Tree.MEM(exp); }
    private static Tree.Exp CALL(Tree.Exp func, List args)
    {	return new Tree.CALL(func, args);   }
    private static Tree.Exp ESEQ(Tree.Stm stm, Tree.Exp exp)
    {
	if (stm == null) return exp;
	return new Tree.ESEQ(stm, exp);
    }

    private static Tree.Stm MOVE(Tree.Exp dst, Tree.Exp src)
    {
	return new Tree.MOVE(dst, src);
    }
    private static Tree.Stm EXP(Tree.Exp exp) {	return new Tree.EXP(exp); }
    private static Tree.Stm JUMP(Label target)
    {	return new Tree.JUMP(target);   }
    private static Tree.Stm CJUMP(int relop, Tree.Exp l, Tree.Exp r,
				  Label t, Label f)
    {	return new Tree.CJUMP(relop, l, r, t, f);   }
    private static Tree.Stm SEQ(Tree.Stm left, Tree.Stm right)
    {
	if (left == null)
	    return right;
	if (right == null)
	    return left;
	return new Tree.SEQ(left, right);
    }
    private static Tree.Stm LABEL(Label label)
    {
	return new Tree.LABEL(label);
    }

    private LinkedList<Frag> frags = new LinkedList<Frag>();
    public List<Frag> results() { return frags; }

    private HashMap<String, CLASS>  classEnv  = new HashMap<String, CLASS>();
    private HashMap<Symbol, Label>  methodEnv = new HashMap<Symbol, Label>();
    private HashMap<String, Label>  staticEnv = new HashMap<String, Label>();
    private HashMap<String, Label>  vtableEnv = new HashMap<String, Label>();
    private Table<Frame.Access>     varEnv    = new Table<Frame.Access>();

    private Label findMethodLabel(CLASS owner, String name)
    {
        for (CLASS c = owner; c != null; c = c.parent) {
            Label label = methodEnv.get(Symbol.symbol(c.name + "__" + name));
            if (label != null) {
                return label;
            }
        }
        throw new IllegalStateException(
            "No translated method for " + owner.name + "." + name);
    }

    public Exp visit(Absyn.Program n)
    {
        frags.clear();
        classEnv.clear();
        methodEnv.clear();
        vtableEnv.clear();
        staticEnv.clear();
        strings.clear();

        // Register declarations before resolving inherited methods.
        for (Absyn.ClassDecl c : n.classes) {
            if (c.checktype == null) {
                throw new IllegalStateException("Class has not been checked: " + c.name);
            }
            classEnv.put(c.name, c.checktype);
            vtableEnv.put(c.name, new Label(c.name + "__vtable"));
            for (Absyn.MethodDecl m : c.methods) {
                String name = c.name + "__" + m.name;
                methodEnv.put(Symbol.symbol(name), new Label(name));
            }
        }

        // Emit virtual tables in source class order.
        for (Absyn.ClassDecl c : n.classes) {
            LinkedList<Label> methods = new LinkedList<Label>();
            for (Types.FIELD member : c.checktype.instance.methods) {
                methods.add(findMethodLabel(c.checktype, member.name));
            }
            frags.add(new DataFrag(
                frame.record(vtableEnv.get(c.name), methods)));
        }

        for (Absyn.ClassDecl c : n.classes) {
            c.accept(this);
        }
        return new Nx(null);
    }

    public Exp visit(Absyn.ClassDecl c)
    {
        CLASS previousClass = currentClass;
        currentClass = c.checktype;
        try {
            for (Absyn.MethodDecl m : c.methods) {
                m.accept(this);
            }
        } finally {
            currentClass = previousClass;
        }
        return new Nx(null);
    }

    public Exp visit(Absyn.ThreadDecl c)
    {
	// TODO
	return null;
    }

    public Exp visit(Absyn.MethodDecl m)
    {
        if (currentClass == null) {
            throw new IllegalStateException("Method has no enclosing class");
        }

        boolean entry = m.returnType == null
            && m.name.equals("main") && !(m instanceof Absyn.VoidDecl);
        String name = entry ? "main" : currentClass.name + "__" + m.name;

        Frame.Frame previousFrame = frame;
        frame = frame.newFrame(Symbol.symbol(name));
        varEnv.beginScope();
        try {
            // The reference reserves this first slot for every method.
            varEnv.put("this", frame.allocFormal());
            for (Absyn.Formal parameter : m.params) {
                parameter.accept(this);
            }

            Tree.Stm body = null;
            for (Absyn.VarDecl local : m.locals) {
                body = SEQ(body, local.accept(this).unNx());
            }
            for (Absyn.Stmt statement : m.stmts) {
                body = SEQ(body, statement.accept(this).unNx());
            }

            Tree.Exp result = m.returnVal == null
                ? CONST(0) : m.returnVal.accept(this).unEx();
            body = SEQ(body, MOVE(TEMP(frame.RV()), result));

            LinkedList<Tree.Stm> statements = new LinkedList<Tree.Stm>();
            statements.add(body);
            frame.procEntryExit1(statements);

            Tree.Stm procedure = null;
            for (Tree.Stm statement : statements) {
                procedure = SEQ(procedure, statement);
            }
            frags.add(new ProcFrag(procedure, frame));
        } finally {
            varEnv.endScope();
            frame = previousFrame;
        }
        return new Nx(null);
    }

    public Exp visit(Absyn.VoidDecl m)
    {
        return visit((Absyn.MethodDecl) m);
    }

    public Exp visit(Absyn.Formal f)
    {
        Frame.Access access = frame.allocFormal();
        varEnv.put(f.name, access);
        return new Nx(null);
    }

    public Exp visit(Absyn.VarDecl v)
    {
        Frame.Access access = frame.allocLocal();
        varEnv.put(v.name, access);
        if (v.init == null) {
            return new Nx(null);
        }
        return new Nx(MOVE(
            access.exp(frame), v.init.accept(this).unEx()));
    }

    public Exp visit(Absyn.BooleanType  t)   { return new Nx(null); }
    public Exp visit(Absyn.IntegerType  t)   { return new Nx(null); }
    public Exp visit(Absyn.ArrayType t)      { return new Nx(null); }
    public Exp visit(Absyn.IdentifierType t) { return new Nx(null); }

    public Exp visit(Absyn.AssignStmt s)
    {
        Tree.Exp target = s.target.accept(this).unEx();
        Tree.Exp value = s.value.accept(this).unEx();
        return new Nx(MOVE(target, value));
    }

    public Exp visit(Absyn.BlockStmt b)
    {
        Tree.Stm body = null;
        for (Absyn.Stmt statement : b.stmts) {
            body = SEQ(body, statement.accept(this).unNx());
        }
        return new Nx(body);
    }

    public Exp visit(Absyn.IfStmt s)
    {
        Exp condition = s.test.accept(this);
        Exp thenPart = s.thenStmt.accept(this);
        Exp elsePart = s.elseStmt == null
            ? null : s.elseStmt.accept(this);
        return new IfThenElseExp(condition, thenPart, elsePart);
    }

    public Exp visit(Absyn.WhileStmt s)
    {
        Label test = new Label();
        Label body = new Label();
        Label done = new Label();

        Tree.Stm code = LABEL(test);
        code = SEQ(code, s.test.accept(this).unCx(body, done));
        code = SEQ(code, LABEL(body));
        code = SEQ(code, s.body.accept(this).unNx());
        code = SEQ(code, JUMP(test));
        code = SEQ(code, LABEL(done));
        return new Nx(code);
    }

    private Exp Ex(int op, Absyn.BinOpExpr e)
    {
        Exp e1 = e.e1.accept(this);
		Exp e2 = e.e2.accept(this);
		return new Ex(BINOP(op, e1.unEx(), e2.unEx()));
    }

    public Exp visit(Absyn.AddExpr e) { return Ex(Tree.BINOP.PLUS, e); }
    public Exp visit(Absyn.DivExpr e) { return Ex(Tree.BINOP.DIV, e); }
    public Exp visit(Absyn.MulExpr e) { return Ex(Tree.BINOP.MUL, e); }
    public Exp visit(Absyn.SubExpr e) { return Ex(Tree.BINOP.MINUS, e); }

    public Exp visit(Absyn.NegExpr e)
    {
	return new Ex(BINOP(Tree.BINOP.MINUS,
			    CONST(0), e.e.accept(this).unEx()));
    }

    public Exp visit(Absyn.AndExpr e)
    {
        Exp e1 = e.e1.accept(this);
	Exp e2 = e.e2.accept(this);
	// Short-circuit Boolean AND, with false == 0.
	return new IfThenElseExp(e1, e2, new Ex(CONST(0)));
    }

    public Exp visit(Absyn.OrExpr e)
    {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return new IfThenElseExp(e1, new Ex(CONST(1)), e2);
    }

    private Exp RelCx(int op, Absyn.BinOpExpr e)
    {
        Exp e1 = e.e1.accept(this);
	Exp e2 = e.e2.accept(this);
	return new RelCx(op, e1.unEx(), e2.unEx());
    }

    public Exp visit(Absyn.GreaterExpr e)  { return RelCx(Tree.CJUMP.GT, e); }
    public Exp visit(Absyn.LesserExpr e)   { return RelCx(Tree.CJUMP.LT, e); }
    public Exp visit(Absyn.EqualExpr e)    { return RelCx(Tree.CJUMP.EQ, e); }
    public Exp visit(Absyn.NotEqExpr e)    { return RelCx(Tree.CJUMP.NE, e); }

    public Exp visit(Absyn.NotExpr e)
    {
        Exp exp = e.e.accept(this);
        return new IfThenElseExp(exp, new Ex(CONST(0)), new Ex(CONST(1)));
    }

    public Exp visit(Absyn.NewArrayExpr e)
    {
        if (e.dimensions.isEmpty() || e.dimensions.getFirst() == null) {
            throw new IllegalStateException("Array allocation needs a size");
        }

        // Allocate the outer dimension; trailing [] dimensions remain unallocated.
        Tree.Exp size = e.dimensions.getFirst().accept(this).unEx();
        Temp length = new Temp();

        LinkedList<Tree.Exp> arguments = new LinkedList<Tree.Exp>();
        arguments.add(TEMP(length));
        arguments.add(TEMP(length));

        return new Ex(ESEQ(
            MOVE(TEMP(length), size),
            frame.externalCall(Symbol.symbol("new"), arguments)));
    }

    private Tree.Exp xinuCall(
        String name, Iterable<Absyn.Expr> arguments)
    {
        LinkedList<Tree.Exp> actuals = new LinkedList<Tree.Exp>();
        for (Absyn.Expr argument : arguments) {
            actuals.add(argument.accept(this).unEx());
        }
        return frame.externalCall(Symbol.symbol(name), actuals);
    }

    public Exp visit(Absyn.XinuCallStmt s)
    {
        return new Nx(EXP(xinuCall(s.method, s.args)));
    }

    public Exp visit(Absyn.XinuCallExpr e)
    {
        return new Ex(xinuCall(e.name, e.args));
    }

    public Exp visit(Absyn.CallExpr e)
    {
        Tree.Exp receiver = e.receiver.accept(this).unEx();
        Temp object = new Temp();
        Label ok = new Label();

        LinkedList<Tree.Exp> arguments = new LinkedList<Tree.Exp>();
        arguments.add(TEMP(object));
        for (Absyn.Expr argument : e.args) {
            arguments.add(argument.accept(this).unEx());
        }

        Tree.Exp vtable = MEM(BINOP(
            Tree.BINOP.PLUS, TEMP(object), CONST(-frame.wordSize())));
        Tree.Exp method = MEM(BINOP(
            Tree.BINOP.PLUS, vtable, CONST(e.typeIndex * frame.wordSize())));

        Tree.Stm check = SEQ(
            MOVE(TEMP(object), receiver),
            SEQ(
                CJUMP(Tree.CJUMP.EQ, TEMP(object), CONST(0),
                      frame.badPtr(), ok),
                LABEL(ok)));

        return new Ex(ESEQ(check, CALL(method, arguments)));
    }

    public Exp visit(Absyn.FalseExpr e)
    {   return new Ex(CONST(0));  }

    public Exp visit(Absyn.IntegerLiteral e)
    {   return new Ex(CONST(e.value));   }

    public Exp visit(Absyn.NullExpr e)
    {   return new Ex(CONST(0));   }

    public Exp visit(Absyn.NewObjectExpr e)
    {
        if (!(e.type instanceof Absyn.IdentifierType)) {
            throw new IllegalStateException("Expected a class type for new");
        }

        String name = ((Absyn.IdentifierType) e.type).id;
        CLASS type = classEnv.get(name);
        Label vtable = vtableEnv.get(name);
        if (type == null || vtable == null) {
            throw new IllegalStateException(
                "Missing allocation information for class: " + name);
        }

        LinkedList<Tree.Exp> arguments = new LinkedList<Tree.Exp>();
        arguments.add(CONST(type.instance.fields.size()));
        arguments.add(NAME(vtable));
        return new Ex(frame.externalCall(Symbol.symbol("new"), arguments));
    }

    private HashMap<String, Label> strings = new HashMap<String, Label>();

    public Exp visit(Absyn.StringLiteral e)
    {
	String result = e.value.intern();
	Label lab = strings.get(result);
	if (lab != null) return new Ex(NAME(lab));
	lab = new Label();
	strings.put(result, lab);

	DataFrag frag = new DataFrag(frame.string(lab, result.intern()));
	frags.add(frag);
	return new Ex(NAME(lab));
    }

    public Exp visit(Absyn.ThisExpr e)
    {
        Frame.Access receiver = varEnv.get("this");
        if (receiver == null) {
            throw new IllegalStateException("No receiver in the current method");
        }
        return new Ex(receiver.exp(frame));
    }

    public Exp visit(Absyn.TrueExpr e)
    {   return new Ex(CONST(1));   }

    public Exp visit(Absyn.FieldExpr e)
    {
        int index = e.typeIndex;
        Tree.Exp exp = e.record.accept(this).unEx();

        Label bad = frame.badPtr();
        Label ok = new Label();
        Temp result = new Temp();

        Tree.Stm check = SEQ(MOVE(TEMP(result), exp),CJUMP(Tree.CJUMP.EQ, TEMP(result), CONST(0), bad, ok));
        return new Ex(ESEQ(
            check,
            ESEQ(
                LABEL(ok),
                MEM(BINOP(
                    Tree.BINOP.PLUS,
                    TEMP(result),
                    CONST(index * frame.wordSize())
                ))
            )
        ));
    }

    public Exp visit(Absyn.IdentifierExpr e)
    {
        Frame.Access access = varEnv.get(e.name);
        if (access != null) {
            return new Ex(access.exp(frame));
        }

        if (currentClass != null) {
            Types.FIELD field = currentClass.instance.fields.get(e.name);
            if (field != null) {
                Absyn.FieldExpr selection =
                    new Absyn.FieldExpr(new Absyn.ThisExpr(), e.name);
                selection.typeIndex = field.index;
                return visit(selection);
            }
        }

        throw new IllegalStateException(
            "No variable or field location for: " + e.name);
    }

    public Exp visit(Absyn.ArrayExpr e)
    {
        Tree.Exp array = e.array.accept(this).unEx();
        Tree.Exp index = e.index.accept(this).unEx();

        Temp base = new Temp();
        Temp subscript = new Temp();
        Label nonnull = new Label();
        Label nonnegative = new Label();
        Label inBounds = new Label();

        Tree.Exp length = MEM(BINOP(
            Tree.BINOP.PLUS, TEMP(base), CONST(-frame.wordSize())));

        Tree.Stm[] checks = {
            MOVE(TEMP(base), array),
            MOVE(TEMP(subscript), index),
            CJUMP(Tree.CJUMP.EQ, TEMP(base), CONST(0),
                  frame.badPtr(), nonnull),
            LABEL(nonnull),
            CJUMP(Tree.CJUMP.LT, TEMP(subscript), CONST(0),
                  frame.badSub(), nonnegative),
            LABEL(nonnegative),
            CJUMP(Tree.CJUMP.GE, TEMP(subscript), length,
                  frame.badSub(), inBounds)
        };

        Tree.Stm code = null;
        for (int i = checks.length - 1; i >= 0; i--) {
            code = SEQ(checks[i], code);
        }

        Tree.Exp element = MEM(BINOP(
            Tree.BINOP.PLUS,
            TEMP(base),
            BINOP(Tree.BINOP.MUL, TEMP(subscript), CONST(frame.wordSize()))));

        return new Ex(ESEQ(code, ESEQ(LABEL(inBounds), element)));
    }
}
