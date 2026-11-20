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
 * Implements a move instruction.
 */

public class MOVE extends Stm 
{
    public Exp dst, src;
    public MOVE(Exp d, Exp s) { dst=d; src=s; }
    public LinkedList<Exp> kids() {
        LinkedList<Exp> children = new LinkedList<Exp>();
        if (dst instanceof MEM) {
            children.add(((MEM) dst).exp);
        }
        children.add(src);
        return children;
    }

    public Stm build(LinkedList<Exp> kids) {
        if (dst instanceof MEM) {
            return new MOVE(new MEM(kids.getFirst()), kids.get(1));
        }
        return new MOVE(dst, kids.getFirst());
    }

    public void accept(IntVisitor v, int d) { v.visit(this, d); }
}
