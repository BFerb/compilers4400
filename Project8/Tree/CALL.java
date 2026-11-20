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
import java.util.List;
import java.util.LinkedList;

/**
 * Implements a procedure call, application of a function to an
 * argument list.
 */

public class CALL extends Exp 
{
    public Exp func;
    public List<Exp> args;
    public CALL(Exp f, List<Exp> a) { func=f; args=a; }
    public LinkedList<Exp> kids() {
        LinkedList<Exp> children = new LinkedList<Exp>();
        children.add(func);
        if (args != null) {
            children.addAll(args);
        }
        return children;
    }

    public Exp build(LinkedList<Exp> kids) {
        LinkedList<Exp> arguments = new LinkedList<Exp>(kids);
        Exp function = arguments.removeFirst();
        return new CALL(function, arguments);
    }

    public void accept(IntVisitor v, int d) { v.visit(this, d); }
}
