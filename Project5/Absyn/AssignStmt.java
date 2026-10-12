package Absyn;

public class AssignStmt extends Stmt
{
    public AssignableExpr target;
    public Expr value;

    public AssignStmt(AssignableExpr target, Expr value)
    {
        this.target = target;
        this.value = value;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}