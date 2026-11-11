/**
 * COSC 4400 - Project #7
 * AST representation and visitor dispatch for compiler passes.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */

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

    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}
