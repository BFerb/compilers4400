package Absyn;

public class FieldExpr extends AssignableExpr
{
    public Expr expression;
    public String field;

    public FieldExpr(Expr expression, jString field)
    {
        this.expression = expression;
        this.field = field;
    }
    public void accept(Visitor v) {v.visit(this); }
}