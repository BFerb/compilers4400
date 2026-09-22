package Absyn;

public class NewObjectExpr extends Expr
{
    public String name;
    
    public NewObjectExpr(String name)
    {
        this.name = name;
    }

    public void accept(Visitor v) {v.visit(this); 
    }
}