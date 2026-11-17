/**
 * COSC 4400 - Project #7
 * Provides abstract temporaries and labels for generated code.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved.  */
package Temp;

/**
 * Class used to create a temp, an abstract name for a local variable.
 */

public class Temp 
{
    private static int count;
    private int num;
    public String toString() {return "t" + num;}
    public Temp() 
	{ 
		num = count++; 
	}
    public int hashCode() { return num; }
	public boolean spillTemp = false;
    public int spillCost;

}
