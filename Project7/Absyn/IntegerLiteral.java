/**
 * COSC 4400 - Project #7
 * AST representation and visitor dispatch for compiler passes.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Integer Literals.
 */

public class IntegerLiteral extends Expr
{
    public int value;

    public IntegerLiteral(int value)
    {
		this.value = value;
    }

    public IntegerLiteral(Integer value)
    {
		this.value = value.intValue();
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}
