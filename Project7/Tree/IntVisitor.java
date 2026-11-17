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

public interface IntVisitor {
    public void visit(SEQ n, int d);
    public void visit(LABEL n, int d);
    public void visit(JUMP n, int d);
    public void visit(CJUMP n, int d);
    public void visit(MOVE n, int d);
    public void visit(BINOP n, int d);
    public void visit(MEM n, int d);
    public void visit(TEMP n, int d);
    public void visit(EXP n, int d);	
    public void visit(ESEQ n, int d);
    public void visit(NAME n, int d);
    public void visit(CONST n, int d);
    public void visit(CALL n, int d);
}
