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
 * Basetype "String".
 */

public class STRING extends Type
{
    public STRING()
    {}

    public String toString()
    { return "STRING:String"; }

    public boolean coerceTo(Type t)
    {   
		if (t instanceof OBJECT)
			return this.coerceTo(((OBJECT)t).myClass);

		if (t instanceof CLASS)
			return ((CLASS)t).name.equals("String");

		return (t instanceof STRING);   
	}

    /** Visitor pattern dispatch. */
    public void accept(Visitor v)
    {   v.visit(this);   }
}
