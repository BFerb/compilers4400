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
 * Declarations for formal method parameters.
 */

public class Formal extends Absyn
{
    public Type type;
    public String name;
    public Types.Type checktype;
    public Formal(Type type, String name)
    {
		this.type = type;
		this.name = name;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}
