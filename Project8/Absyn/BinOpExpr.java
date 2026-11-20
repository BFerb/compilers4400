/**
 * COSC 4400 - Project #7
 * Represents and visits MiniJava abstract syntax trees.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
package Absyn;

public abstract class BinOpExpr extends Expr
{
    public Expr e1;
    public Expr e2;


    public BinOpExpr(Expr e1, Expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }

    public abstract void accept(Visitor v);

}

