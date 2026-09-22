package Absyn;

public class FieldExpr extends AssignableExpr
{
    public Expr record;
    public String field;

    public FieldExpr(Expr record, String field)
    {
        this.record = record;
        this.field = field;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}