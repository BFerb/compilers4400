package Absyn;

public class NewArrayExpr extends Expr
{
    public Expr e;

    public NewArrayExpr(Expr e)
    {
        this.e = e;
    }

    public void accept(Visitor v) {v.visit(this); 
    }
}