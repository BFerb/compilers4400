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
import java.util.LinkedList;
import Temp.Temp;

/**
 * Abstract class that represents an expression in the Tree language.
 */

public abstract class Exp implements Hospitable
{
    public abstract void accept(IntVisitor v, int d);
}
