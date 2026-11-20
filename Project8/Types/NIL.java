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
 * Base type for "null".
 */

public class NIL extends Type
{
    public NIL()
    {}

    public String toString()
    { return "NIL"; }

    public boolean coerceTo(Type t)
    {   return ( (t instanceof NIL) 
		 || (t instanceof ARRAY)
		 || (t instanceof OBJECT) );   
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v)
    {   v.visit(this);   }
}
