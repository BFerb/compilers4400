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
 * Interface for Visitor Pattern traversals.
 */

public interface Visitor
{
    /** Visitor pattern dispatch. */
    public void visit(ARRAY a);
    public void visit(BOOLEAN b);
    public void visit(CLASS c);
    public void visit(FIELD f);
    public void visit(FUNCTION f);
    public void visit(INT i);
    public void visit(NIL n);
    public void visit(OBJECT o);
    public void visit(RECORD r);
    public void visit(STRING s);
    public void visit(VOID v);
}
