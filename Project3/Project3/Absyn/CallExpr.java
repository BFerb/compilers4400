package Absyn;

import java.util.AbstractList;

public class CallExpr extends Expr
{
    public Expr receiver;
    public String method;
    public AbstractList<Expr> args;

    public CallExpr(Expr receiver, String method, AbstractList<Expr> args)
    {
        this.receiver = receiver;
        this.method = method;
        this.args = args;
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }
}