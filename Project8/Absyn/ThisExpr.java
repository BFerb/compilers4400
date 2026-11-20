/**
 * COSC 4400 - Project #7
 * AST representation and visitor dispatch for compiler passes.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
package Absyn;

public class ThisExpr extends Expr
{
    public ThisExpr()
    {
    }

    public void accept(Visitor v) {v.visit(this); 
    }

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}