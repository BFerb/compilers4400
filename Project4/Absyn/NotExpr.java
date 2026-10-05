package Absyn;

public class NotExpr extends Expr
{
    public Expr e;
    
    public NotExpr(Expr e)
    {
        this.e = e;
    }

    public void accept(Visitor v) {v.visit(this); 
    }
    
}