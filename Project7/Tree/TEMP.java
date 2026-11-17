/**
 * COSC 4400 - Project #7
 * Represents or prints intermediate representation trees.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved.  */
package Tree;
import Temp.Temp;
import java.util.LinkedList;

/**
 * A temporary t.
 */

public class TEMP extends Exp 
{
    public Temp temp;
    public TEMP(Temp t) 
	{ 
		temp=t; 
		//System.out.println("\n Call to TEMP : "+t.toString());
	}
    public void accept(IntVisitor v, int d) { v.visit(this, d); }
}
