package Absyn;

import java.util.LinkedList;

public class NewArrayExpr extends Expr
{
    public Type type;
    public LinkedList<Expr> dimensions;

    public NewArrayExpr(Type type, LinkedList<Expr> dimensions)
    {
        this.type = type;
        this.dimensions = dimensions;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}
