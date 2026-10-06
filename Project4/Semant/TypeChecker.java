/**
 * COSC 4400 - Project #4
 * Performs semantic type checking for MiniJava programs.
 * @authors [Nick Grons, Ben Ferber]
 * Instructor [Dr. Brylow]
 * TA-BOT:MAILTO [nicholas.grons@marquette.edu] [benjamin.ferber@marquette.edu]
 */

package Semant;

import java.util.Iterator;

public class TypeChecker
{
    private Symbol.Table<Types.CLASS> classEnv;
    private Symbol.Table<Types.Type> vars;
    private Types.CLASS currentClass;
    private boolean hadError = false;

    public TypeChecker(Symbol.Table<Types.CLASS> classEnv)
    {
        this.classEnv = classEnv;
        this.vars = new Symbol.Table<Types.Type>();
    }

    public void check(Absyn.Program program)
    {
        for (Absyn.ClassDecl cd : program.classes)
            checkClass(cd);
    }

    private void error(String message)
    {
        hadError = true;
        System.err.println("Type error: " + message);
    }

    public boolean hasErrors()
    {
        return hadError;
    }

    private Types.Type convertType(Absyn.Type type)
    {
        if (type == null)
            return new Types.VOID();

        if (type instanceof Absyn.IntegerType)
            return new Types.INT();

        if (type instanceof Absyn.BooleanType)
            return new Types.BOOLEAN();

        if (type instanceof Absyn.ArrayType)
        {
            Absyn.ArrayType a = (Absyn.ArrayType)type;
            return new Types.ARRAY(convertType(a.base));
        }

        if (type instanceof Absyn.IdentifierType)
        {
            Absyn.IdentifierType i = (Absyn.IdentifierType)type;
            Types.CLASS c = classEnv.get(i.id);

            if (c == null)
            {
                error("unknown class " + i.id);
                return new Types.NIL();
            }

            return c.instance;
        }

        error("unknown type");
        return new Types.NIL();
    }

    private void checkClass(Absyn.ClassDecl cd)
    {
        currentClass = cd.checktype;

        for (Absyn.MethodDecl md : cd.methods)
            checkMethod(md);
    }

    private void checkMethod(Absyn.MethodDecl md)
    {
        vars.beginScope();

        if (currentClass != null && currentClass.instance.fields != null)
        {
            for (Types.FIELD f : currentClass.instance.fields)
                vars.put(f.name, f.type);
        }

        for (Absyn.Formal f : md.params)
            vars.put(f.name, f.checktype);

        for (Absyn.VarDecl local : md.locals)
        {
            vars.put(local.name, local.checktype);

            if (local.init != null)
            {
                Types.Type initType = checkExpr(local.init);

                if (!compatible(initType, local.checktype))
                    error("cannot initialize " + local.name + " with " + initType);
            }
        }

        for (Absyn.Stmt stmt : md.stmts)
            checkStmt(stmt);

        if (!(md.checktype.result instanceof Types.VOID))
        {
            Types.Type actual = checkExpr(md.returnVal);

            if (!compatible(actual, md.checktype.result))
                error("bad return type in method " + md.name);
        }

        vars.endScope();
    }

    private boolean compatible(Types.Type from, Types.Type to)
    {
        return from != null && to != null && from.coerceTo(to);
    }

    private void checkStmt(Absyn.Stmt stmt)
    {
        if (stmt == null)
            return;

        if (stmt instanceof Absyn.AssignStmt)
        {
            Absyn.AssignStmt s = (Absyn.AssignStmt)stmt;

            Types.Type lhs = checkExpr(s.target);
            Types.Type rhs = checkExpr(s.value);

            if (!compatible(rhs, lhs))
                error("incompatible assignment");
        }
        else if (stmt instanceof Absyn.BlockStmt)
        {
            Absyn.BlockStmt s = (Absyn.BlockStmt)stmt;

            for (Absyn.Stmt child : s.stmts)
                checkStmt(child);
        }
        else if (stmt instanceof Absyn.IfStmt)
        {
            Absyn.IfStmt s = (Absyn.IfStmt)stmt;

            requireBoolean(checkExpr(s.test), "if condition");
            checkStmt(s.thenStmt);
            checkStmt(s.elseStmt);
        }
        else if (stmt instanceof Absyn.WhileStmt)
        {
            Absyn.WhileStmt s = (Absyn.WhileStmt)stmt;

            requireBoolean(checkExpr(s.test), "while condition");
            checkStmt(s.body);
        }
        else if (stmt instanceof Absyn.XinuCallStmt)
        {
            checkXinuStmt((Absyn.XinuCallStmt)stmt);
        }
    }

    private Types.Type checkExpr(Absyn.Expr expr)
    {
        if (expr == null)
            return new Types.VOID();

        if (expr instanceof Absyn.IntegerLiteral)
            return new Types.INT();

        if (expr instanceof Absyn.StringLiteral)
            return new Types.STRING();

        if (expr instanceof Absyn.TrueExpr ||
            expr instanceof Absyn.FalseExpr)
            return new Types.BOOLEAN();

        if (expr instanceof Absyn.NullExpr)
            return new Types.NIL();

        if (expr instanceof Absyn.ThisExpr)
            return currentClass.instance;

        if (expr instanceof Absyn.IdentifierExpr)
        {
            Absyn.IdentifierExpr e = (Absyn.IdentifierExpr)expr;
            Types.Type type = vars.get(e.name);

            if (type == null)
            {
                error("undefined identifier " + e.name);
                return new Types.NIL();
            }

            return type;
        }

        if (expr instanceof Absyn.AddExpr ||
            expr instanceof Absyn.SubExpr ||
            expr instanceof Absyn.MulExpr ||
            expr instanceof Absyn.DivExpr)
        {
            Absyn.BinOpExpr e = (Absyn.BinOpExpr)expr;

            requireInt(checkExpr(e.e1), "arithmetic operand");
            requireInt(checkExpr(e.e2), "arithmetic operand");

            return new Types.INT();
        }

        if (expr instanceof Absyn.GreaterExpr ||
            expr instanceof Absyn.LesserExpr)
        {
            Absyn.BinOpExpr e = (Absyn.BinOpExpr)expr;

            requireInt(checkExpr(e.e1), "comparison operand");
            requireInt(checkExpr(e.e2), "comparison operand");

            return new Types.BOOLEAN();
        }

        if (expr instanceof Absyn.AndExpr ||
            expr instanceof Absyn.OrExpr)
        {
            Absyn.BinOpExpr e = (Absyn.BinOpExpr)expr;

            requireBoolean(checkExpr(e.e1), "boolean operand");
            requireBoolean(checkExpr(e.e2), "boolean operand");

            return new Types.BOOLEAN();
        }

        if (expr instanceof Absyn.EqualExpr ||
            expr instanceof Absyn.NotEqExpr)
        {
            Absyn.BinOpExpr e = (Absyn.BinOpExpr)expr;

            Types.Type left = checkExpr(e.e1);
            Types.Type right = checkExpr(e.e2);

            if (!compatible(left, right) && !compatible(right, left))
                error("incompatible equality operands");

            return new Types.BOOLEAN();
        }

        if (expr instanceof Absyn.NegExpr)
        {
            Absyn.NegExpr e = (Absyn.NegExpr)expr;
            requireInt(checkExpr(e.e), "negation operand");
            return new Types.INT();
        }

        if (expr instanceof Absyn.NotExpr)
        {
            Absyn.NotExpr e = (Absyn.NotExpr)expr;
            requireBoolean(checkExpr(e.e), "not operand");
            return new Types.BOOLEAN();
        }

        if (expr instanceof Absyn.ArrayExpr)
        {
            Absyn.ArrayExpr e = (Absyn.ArrayExpr)expr;

            Types.Type arrayType = checkExpr(e.array);
            requireInt(checkExpr(e.index), "array index");

            if (!(arrayType instanceof Types.ARRAY))
            {
                error("indexing non-array value");
                return new Types.NIL();
            }

            return ((Types.ARRAY)arrayType).element;
        }

        if (expr instanceof Absyn.FieldExpr)
            return checkField((Absyn.FieldExpr)expr);

        if (expr instanceof Absyn.CallExpr)
            return checkCall((Absyn.CallExpr)expr);

        if (expr instanceof Absyn.NewObjectExpr)
        {
            Absyn.NewObjectExpr e = (Absyn.NewObjectExpr)expr;
            return convertType(e.type);
        }

        if (expr instanceof Absyn.NewArrayExpr)
        {
            Absyn.NewArrayExpr e = (Absyn.NewArrayExpr)expr;

            for (Absyn.Expr dimension : e.dimensions)
            {
                if (dimension != null)
                    requireInt(checkExpr(dimension), "array dimension");
            }

            Types.Type result = convertType(e.type);

            for (int i = 0; i < e.dimensions.size(); i++)
                result = new Types.ARRAY(result);

            return result;
        }

        if (expr instanceof Absyn.XinuCallExpr)
            return checkXinuExpr((Absyn.XinuCallExpr)expr);

        error("unknown expression " + expr.getClass().getSimpleName());
        return new Types.NIL();
    }

    private Types.Type checkField(Absyn.FieldExpr expr)
    {
        Types.Type receiver = checkExpr(expr.record);

        if (!(receiver instanceof Types.OBJECT))
        {
            error("field access on non-object");
            return new Types.NIL();
        }

        Types.OBJECT object = (Types.OBJECT)receiver;

        if (object.fields == null)
        {
            error("unknown field " + expr.field);
            return new Types.NIL();
        }

        Types.FIELD field = object.fields.get(expr.field);

        if (field == null)
        {
            error("unknown field " + expr.field);
            return new Types.NIL();
        }

        return field.type;
    }

    private Types.Type checkCall(Absyn.CallExpr expr)
    {
        Types.Type receiver = checkExpr(expr.receiver);

        if (!(receiver instanceof Types.OBJECT))
        {
            error("method call on non-object");
            return new Types.NIL();
        }

        Types.OBJECT object = (Types.OBJECT)receiver;

        if (object.methods == null)
        {
            error("unknown method " + expr.method);
            return new Types.NIL();
        }

        Types.FIELD methodField = object.methods.get(expr.method);

        if (methodField == null ||
            !(methodField.type instanceof Types.FUNCTION))
        {
            error("unknown method " + expr.method);
            return new Types.NIL();
        }

        expr.typeIndex = methodField.index;

        Types.FUNCTION function = (Types.FUNCTION)methodField.type;

        Iterator<Types.FIELD> formals = function.formals.iterator();
        Iterator<Absyn.Expr> actuals = expr.args.iterator();

        while (formals.hasNext() && actuals.hasNext())
        {
            Types.FIELD formal = formals.next();
            Types.Type actual = checkExpr(actuals.next());

            if (!compatible(actual, formal.type))
                error("bad argument type in call to " + expr.method);
        }

        if (formals.hasNext() || actuals.hasNext())
            error("wrong number of arguments in call to " + expr.method);

        return function.result;
    }

    private void checkXinuStmt(Absyn.XinuCallStmt call)
    {
        String name = call.method;

        if (name.equals("print"))
        {
            if (call.args.size() != 1)
                error("Xinu.print expects one argument");
            else
            {
                Types.Type t = checkExpr(call.args.get(0));

                if (!(t instanceof Types.STRING))
                    error("Xinu.print expects String");
            }
        }
        else if (name.equals("printint"))
        {
            if (call.args.size() != 1)
                error("Xinu.printint expects one argument");
            else
                requireInt(checkExpr(call.args.get(0)), "Xinu.printint argument");
        }
        else if (name.equals("println") || name.equals("yield"))
        {
            if (call.args.size() != 0)
                error("Xinu." + name + " expects no arguments");
        }
        else if (name.equals("sleep"))
        {
            if (call.args.size() != 1)
                error("Xinu.sleep expects one argument");
            else
                requireInt(checkExpr(call.args.get(0)), "Xinu.sleep argument");
        }
        else if (name.equals("threadCreate"))
        {
            if (call.args.size() != 1)
                error("Xinu.threadCreate expects one argument");
            else
                checkExpr(call.args.get(0));
        }
        else
        {
            error("unknown Xinu statement " + name);
        }
    }

    private Types.Type checkXinuExpr(Absyn.XinuCallExpr call)
    {
        if (call.name.equals("readint"))
        {
            if (call.args.size() != 0)
                error("Xinu.readint expects no arguments");

            return new Types.INT();
        }

        error("unknown Xinu expression " + call.name);

        for (Absyn.Expr arg : call.args)
            checkExpr(arg);

        return new Types.NIL();
    }

    private void requireInt(Types.Type type, String context)
    {
        if (!(type instanceof Types.INT))
            error(context + " must be int");
    }

    private void requireBoolean(Types.Type type, String context)
    {
        if (!(type instanceof Types.BOOLEAN))
            error(context + " must be boolean");
    }
}
