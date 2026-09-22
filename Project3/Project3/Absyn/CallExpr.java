package Absyn;

import java.util.AbstractList;

public class CallExpr extends Expe
{
    public Expr receiver;
    public String method;
    public AbstractList<Expr> args;

    public CallExpr(Expr target, String method, AbstractList<Expr> args)
    {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    public void accept(Visitor v) {v.visit(this); 
    }
}