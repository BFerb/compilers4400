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
 * Parent class of all abstract syntax tree nodes.
 */

public abstract class Absyn implements Visitable
{
    /** Visitor pattern dispatch. */
    public abstract void accept(Visitor v);

    public abstract Translate.Exp accept(TreeVisitor v);
}
