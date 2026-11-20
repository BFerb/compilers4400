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
 * Basetype "boolean".
 */

public class BOOLEAN extends Type
{
    public BOOLEAN()
    {}

    public String toString()
    { return "boolean"; }

    public boolean coerceTo(Type t)
    {   return (t instanceof BOOLEAN);   }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v)
    {   v.visit(this);   }
}
