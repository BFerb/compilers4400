/**
 * COSC 4400 - Project #7
 * Represents MiniJava types and class member information.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Types;

/**
 * Parent class of all type descriptors.
 */

public abstract class Type implements Visitable
{
    public abstract String toString();
    public abstract boolean coerceTo(Type t);
    /** Visitor pattern dispatch. */
    public abstract void accept(Visitor v);
}
