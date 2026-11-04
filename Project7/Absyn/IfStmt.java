/**
 * COSC 4400 - Project #7
 * AST representation and visitor dispatch for compiler passes.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
package Absyn;

public class IfStmt extends Stmt
{
    public Expr test;
    public Stmt thenStmt;
    public Stmt elseStmt;

    public IfStmt(Expr test, Stmt thenStmt, Stmt elseStmt)
    {
        this.test = test;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}