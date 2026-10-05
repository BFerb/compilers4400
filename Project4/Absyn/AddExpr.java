/**
 * COSC 4400 - Project #3
 * Explain briefly the functionality of the program.
 * @authors [Nick Grons, Ben Ferber]
 * Instructor [Dr. Brylow]
 * TA-BOT:MAILTO [nicholas.grons@marquette.edu] [benjamin.ferber@marquette.edu]
 */

package Absyn;

public class AddExpr extends BinOpExpr
{
    public AddExpr(Expr e1, Expr e2)
    {
        super(e1, e2);
    }

    public void accept(Visitor v) {v.visit(this); }
}
