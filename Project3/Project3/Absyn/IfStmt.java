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
}