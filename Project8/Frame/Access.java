/**
 * COSC 4400 - Project #7
 * Defines stack frame and variable access interfaces.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved.  */
package Frame;

/**
 * Abstract data type used to state formals and locals that may 
 * be in the registers or in the frame.
 */

public abstract class Access 
{
	public abstract String toString();
	public abstract Tree.Exp exp(Frame f);
	public abstract Tree.Exp exp(Tree.Exp e);
}
