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
 * The symbolic constant of the label.
 */

public class NAME extends Exp 
{
    public Label label;
    public NAME(Label label) 
	{ 
		//System.out.println("\n NAME is called : l :"+l);
		this.label = label; 
	}
    public void accept(IntVisitor v, int d) { v.visit(this, d); }

    public LinkedList<Exp> kids() {
        return new LinkedList<Exp>();
    }

    public Exp build(LinkedList<Exp> kids) {
        return this;
    }
}
