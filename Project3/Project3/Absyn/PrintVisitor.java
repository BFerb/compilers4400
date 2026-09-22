/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;
import java.io.PrintWriter;

/**
 * Visitor prints AST in reparseable form.
 */

public class PrintVisitor implements Visitor
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
		for(int i = 0; i < indentCount; i++)
	    { out.print(' '); }
    }

    /** Visitor pattern dispatch. */
    //public void visit(Absyn ast) {}

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
		    {   indent();   out.print("null");   }
			else
		    {   ((Visitable)o).accept(this);    }	
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
		indentCount--;
		out.print(")");
    }

    public void visit(MethodDecl ast)
    {
		indent();
		out.print("MethodDecl(");
		indentCount++;
		if (null != ast.returnType)
	    { ast.returnType.accept(this); }
		else
	    { out.print("public_static_void"); }
		if (ast.synced) { out.print(" synchronized"); }
		out.print(" " + ast.name);
		visit(ast.params);
        visit(ast.locals);
		visit(ast.stmts);
		ast.returnVal.accept(this);
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

    public void visit(VarDecl ast)
    {
		indent();
		out.print("VarDecl(");
		ast.type.accept(this);
		out.print(" " + ast.name);
		if (null == ast.init)
	    {   out.print(" null");   } 
		else
	    { ast.init.accept(this); }
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
		ast.e.accept(this);
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
		out.print(ast.field);
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
		out.print("TrueExpr()");
	}

	public void visit(FalseExpr ast)
	{
		indent();
		out.print("FalseExpr()");
	}

	public void visit(NullExpr ast)
	{
		indent();
		out.print("NullExpr()");
	}

	public void visit(ThisExpr ast)
	{
		indent();
		out.print("ThisExpr()");
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
		ast.e.accept(this);
		indentCount--;
		out.print(")");
	}

	public void visit(NewObjectExpr ast)
	{
		indent();
		out.print("NewObjectExpr(");
		out.print(ast.name);
		out.print(")");
	}
	
	public void visit(CallExpr ast)
	{
		indent();
		out.print("CallExpr(");
		indentCount++;
		ast.receiver.accept(this);
		out.print(ast.method);
		ast.args.accept(this);
		indentCount--;
		out.print(")");
	}

	public void visit(XinuCallExpr ast)
	{
		indent();
		out.print("XinuCallExpr(");
		indentCount++;
		out.print(ast.method);
		ast.args.accept(this);
		indentCount--;
		out.print(")");
	}
}

