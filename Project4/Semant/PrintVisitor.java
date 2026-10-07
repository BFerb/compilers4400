/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Semant;

import Absyn.*;

import java.io.PrintWriter;

public class PrintVisitor implements Visitor, Types.Visitor
{
    PrintWriter out;
    public int indentCount = 0;

    public PrintVisitor(PrintWriter out)
    {
        this.out = out;
    }

    public PrintVisitor()
    {
        this.out = new PrintWriter(System.out);
    }

    private void indent()
    {
        out.print('\n');

        for (int i = 0; i < indentCount; i++)
        {
            out.print(' ');
        }
    }

    public void visit(Program ast)
    {
        out.print("Program(");
        indentCount++;
        visit(ast.classes);
        indentCount--;
        out.println(")");
        out.flush();
    }

    public void visit(java.util.AbstractList list)
    {
        if (null == list)
        {
            indent();
            out.print("null");
            return;
        }

        indent();
        out.print("AbstractList(");
        indentCount++;

        for (Object o : list)
        {
            if (null == o)
            {
                indent();
                out.print("null");
            }
            else
            {
                ((Visitable)o).accept(this);
            }
        }

        out.print(")");
        indentCount--;
    }

    public void visit(ClassDecl ast)
    {
        indent();
        out.print("ClassDecl(");
        indentCount++;

        out.print(ast.name + " " + ast.parent);
        visit(ast.fields);
        visit(ast.methods);

        if (ast.checktype != null)
            ast.checktype.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(MethodDecl ast)
    {
        indent();
        out.print("MethodDecl(");
        indentCount++;

        if (null != ast.returnType)
        {
            ast.returnType.accept(this);
        }
        else
        {
            out.print("public_static_void");
        }

        if (ast.synced)
        {
            out.print(" synchronized");
        }

        out.print(" " + ast.name);

        visit(ast.params);
        visit(ast.locals);
        visit(ast.stmts);

        ast.returnVal.accept(this);

        if (ast.checktype != null)
            ast.checktype.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(Formal ast)
    {
        indent();
        out.print("Formal(");
        ast.type.accept(this);
        out.print(" " + ast.name + ")");
    }

    public void visit(IdentifierType ast)
    {
        out.print("IdentifierType(" + ast.id + ")");
    }

    public void visit(IntegerType ast)
    {
        out.print("IntegerType");
    }

    public void visit(BooleanType ast)
    {
        out.print("BooleanType");
    }

    public void visit(VoidDecl ast)
    {
    indent();
    out.print("VoidDecl(");
    indentCount++;

    out.print(ast.name);
    visit(ast.locals);
    visit(ast.stmts);

    indentCount--;
    out.print(")");
    }

    public void visit(VarDecl ast)
    {
        indent();
        out.print("VarDecl(");

        ast.type.accept(this);

        out.print(" " + ast.name);

        if (null == ast.init)
        {
            out.print(" null");
        }
        else
        {
            ast.init.accept(this);
        }

        out.print(" ");

        if (ast.checktype != null)
        {
            indentCount++;
            ast.checktype.accept(this);
            indentCount--;
        }

        out.print(")");
    }

    public void visit(XinuCallStmt ast)
    {
        indent();
        out.print("XinuCallStmt(");
        indentCount++;

        out.print(ast.method);
        visit(ast.args);

        indentCount--;
        out.print(")");
    }

    public void visit(IntegerLiteral ast)
    {
        indent();
        out.print("IntegerLiteral(" + ast.value + ")");
    }

    public void visit(StringLiteral ast)
    {
        indent();
        out.print("StringLiteral(" + ast.value + ")");
    }

    public void visit(ArrayType ast)
    {
        out.print("ArrayType(");
        ast.base.accept(this);
        out.print(")");
    }

    public void visit(AddExpr ast)
    {
        indent();
        out.print("AddExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(AndExpr ast)
    {
        indent();
        out.print("AndExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(DivExpr ast)
    {
        indent();
        out.print("DivExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(EqualExpr ast)
    {
        indent();
        out.print("EqualExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(GreaterExpr ast)
    {
        indent();
        out.print("GreaterExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(MulExpr ast)
    {
        indent();
        out.print("MulExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(NotEqExpr ast)
    {
        indent();
        out.print("NotEqExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(OrExpr ast)
    {
        indent();
        out.print("OrExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(SubExpr ast)
    {
        indent();
        out.print("SubExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(LesserExpr ast)
    {
        indent();
        out.print("LesserExpr(");
        indentCount++;

        ast.e1.accept(this);
        ast.e2.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(IdentifierExpr ast)
    {
        indent();
        out.print("IdentifierExpr(");
        out.print(ast.name);
        out.print(")");
    }

    public void visit(FieldExpr ast)
    {
        indent();
        out.print("FieldExpr(");
        indentCount++;

        ast.record.accept(this);

        indent();
        out.print(ast.field);

        indent();
        out.print(ast.typeIndex);

        indentCount--;
        out.print(")");
    }

    public void visit(ArrayExpr ast)
    {
        indent();
        out.print("ArrayExpr(");
        indentCount++;

        ast.array.accept(this);
        ast.index.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(TrueExpr ast)
    {
    indent();
    out.print("TrueExpr");
    }

public void visit(FalseExpr ast)
    {
    indent();
    out.print("FalseExpr");
    }

public void visit(NullExpr ast)
    {
    indent();
    out.print("NullExpr");
    }

public void visit(ThisExpr ast)
    {
    indent();
    out.print("ThisExpr");
    }   

    public void visit(NegExpr ast)
    {
        indent();
        out.print("NegExpr(");
        indentCount++;

        ast.e.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(NotExpr ast)
    {
        indent();
        out.print("NotExpr(");
        indentCount++;

        ast.e.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(NewArrayExpr ast)
    {
    indent();
    out.print("NewArrayExpr(");
    indentCount++;

    ast.type.accept(this);
    visit(ast.dimensions);

    indentCount--;
    out.print(")");
    }

public void visit(NewObjectExpr ast)
    {
    indent();
    out.print("NewObjectExpr(");
    ast.type.accept(this);
    out.print(")");
    }

    public void visit(CallExpr ast)
    {
        indent();
        out.print("CallExpr(");
        indentCount++;

        ast.receiver.accept(this);

        indent();
        out.print(ast.method);

        visit(ast.args);

        indent();
        out.print(ast.typeIndex);

        indentCount--;
        out.print(")");
    }

    public void visit(XinuCallExpr ast)
    {
    indent();
    out.print("XinuCallExpr(");
    indentCount++;

    out.print(ast.name);
    visit(ast.args);

    indentCount--;
    out.print(")");
    }

    public void visit(AssignStmt ast)
    {
        indent();
        out.print("AssignStmt(");
        indentCount++;

        ast.target.accept(this);
        ast.value.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(BlockStmt ast)
    {
        indent();
        out.print("BlockStmt(");
        indentCount++;

        visit(ast.stmts);

        indentCount--;
        out.print(")");
    }

    public void visit(IfStmt ast)
    {
        indent();
        out.print("IfStmt(");
        indentCount++;

        ast.test.accept(this);
        ast.thenStmt.accept(this);

        if (ast.elseStmt != null)
        {
            ast.elseStmt.accept(this);
        }
        else
        {
            indent();
            out.print("null");
        }

        indentCount--;
        out.print(")");
    }

    public void visit(WhileStmt ast)
    {
        indent();
        out.print("WhileStmt(");
        indentCount++;

        ast.test.accept(this);
        ast.body.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(ThreadDecl ast)
    {
        indent();
        out.print("ThreadDecl(");
        indentCount++;

        out.print(ast.name + " " + ast.parent);
        visit(ast.fields);
        visit(ast.methods);

        indentCount--;
        out.print(")");
    }

    public void visit(Types.INT t)
    {
        indent();
        out.print("INT");
    }

    public void visit(Types.BOOLEAN t)
    {
        indent();
        out.print("BOOLEAN");
    }

    public void visit(Types.STRING t)
    {
        indent();
        out.print("STRING");
    }

    public void visit(Types.NIL t)
    {
        indent();
        out.print("NIL");
    }

    public void visit(Types.VOID t)
    {
        indent();
        out.print("VOID");
    }

    public void visit(Types.ARRAY t)
    {
        indent();
        out.print("ARRAY(");
        indentCount++;
        t.element.accept(this);
        indentCount--;
        out.print(")");
    }

    public void visit(Types.FIELD t)
    {
        indent();
        out.print("FIELD(" + t.index + " " + t.name);
        indentCount++;
        t.type.accept(this);
        indentCount--;
        out.print(")");
    }

    public void visit(Types.RECORD t)
    {
        indent();
        out.print("RECORD(");
        indentCount++;
        for (Types.FIELD f : t)
            f.accept(this);
        indentCount--;
        out.print(")");
    }

    public void visit(Types.FUNCTION t)
    {
        indent();
        out.print("FUNCTION(" + t.name);
        indentCount++;

        indent();
        out.print("OBJECT(" + ((Types.OBJECT)t.self).myClass.name + ")");

        t.formals.accept(this);
        t.result.accept(this);

        indentCount--;
        out.print(")");
    }

    public void visit(Types.OBJECT t)
    {
        indent();
        out.print("OBJECT(" + t.myClass.name + ")");
    }

    public void visit(Types.CLASS t)
    {
        indent();
        out.print("CLASS(" + t.name);
        indentCount++;

        indent();
        if (t.parent == null)
            out.print("null");
        else
            out.print(t.parent.name);

        t.methods.accept(this);
        t.fields.accept(this);

        indent();
        out.print("OBJECT(" + t.instance.myClass.name);
        indentCount++;

        if (t.instance.methods != null)
            t.instance.methods.accept(this);

        if (t.instance.fields != null)
            t.instance.fields.accept(this);

        indentCount--;
        out.print(")");

        indentCount--;
        out.print(")");
        out.println();
        out.flush();
    }

}
