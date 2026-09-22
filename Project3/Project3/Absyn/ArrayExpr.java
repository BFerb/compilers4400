package Absyn;

public class ArrayExpr extends AssignableExpr
{
    public Expr array;
    public Expr index;
    
    public ArrayExpr(Expr array, Expr index)
    {

        this.array = array;
        this.index = index;
    }

    public void accept(Visitor v) {v.visit(this); 
    }
}
