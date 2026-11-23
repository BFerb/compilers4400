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
 * Implements a conditional jump.
 */

public class CJUMP extends Stm 
{
    public int relop;
    public Exp left, right;
    public Label iftrue, iffalse;
    public CJUMP(int rel, Exp l, Exp r, Label t, Label f) 
	{
		relop=rel; left=l; right=r; iftrue=t; iffalse=f;
    }

    public final static int EQ=0, NE=1, LT=2, GT=3, LE=4, GE=5,
		ULT=6, ULE=7, UGT=8, UGE=9;

    public static int notRel(int relop) {
        switch (relop) {
            case EQ:  return NE;
            case NE:  return EQ;
            case LT:  return GE;
            case GE:  return LT;
            case GT:  return LE;
            case LE:  return GT;
            case ULT: return UGE;
            case UGE: return ULT;
            case UGT: return ULE;
            case ULE: return UGT;
            default: throw new IllegalArgumentException("Unknown CJUMP relation: " + relop);
        }
    }

    public void accept(CodeVisitor v) { v.visit(this); }
    public void accept(IntVisitor v, int d) { v.visit(this, d); }

    public LinkedList<Exp> kids() {
        LinkedList<Exp> children = new LinkedList<Exp>();
        children.add(left);
        children.add(right);
        return children;
    }

    public Stm build(LinkedList<Exp> kids) {
        return new CJUMP(relop, kids.get(0), kids.get(1), iftrue, iffalse);
    }
}
