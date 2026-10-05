package Absyn;

import java.util.AbstractList;

public class XinuCallExpr extends Expr
{
    public String name;
    public AbstractList<Expr> args;

    public XinuCallExpr(String name, AbstractList<Expr> args)
    {
        this.name = name;
        this.args = args;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}