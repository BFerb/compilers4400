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
 * Interface for nodes that permit Visitor Pattern traversals.
 */

public interface Visitable
{
    /** Visitor pattern dispatch. */
    public void accept(Visitor v);
}
