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
 * An abstract class that represents a statment in the Tree language.
 */

abstract public class Stm implements Hospitable
{
    public abstract void accept(CodeVisitor v);
    public abstract void accept(IntVisitor v, int d);

    public abstract LinkedList<Exp> kids();

    public abstract Stm build(LinkedList<Exp> kids);
}
