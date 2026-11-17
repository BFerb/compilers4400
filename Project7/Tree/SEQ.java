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
import Temp.Label;
import java.util.LinkedList;

/**
 * Sequence: statement 1 followed by statment 2.
 */

public class SEQ extends Stm 
{
    public Stm left, right;
    public SEQ(Stm l, Stm r) { left=l; right=r; }
    public void accept(IntVisitor v, int d) { v.visit(this, d); }
}
