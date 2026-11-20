/**
 * COSC 4400 - Project #7
 * Represents and visits MiniJava abstract syntax trees.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Statement abstract class.
 */

public abstract class Stmt extends Absyn
{
    /** Visitor pattern dispatch. */
    public abstract void accept(Visitor v);
}
