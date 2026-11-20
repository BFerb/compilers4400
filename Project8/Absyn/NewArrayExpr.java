/**
 * COSC 4400 - Project #7
 * AST representation and visitor dispatch for compiler passes.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
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

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}
