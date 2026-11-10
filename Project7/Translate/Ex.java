/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved.  */
package Translate;
import Temp.Label;

/** A translated expression represented as a Tree.Exp. */
public class Ex extends Exp {
    Tree.Exp exp;

    Ex(Tree.Exp e) { exp = e; }

    Tree.Exp unEx() { return exp; }

    Tree.Stm unNx() {
        if (exp instanceof Tree.CONST) {
            return null;
        }
        return new Tree.EXP(exp);
    }

    Tree.Stm unCx(Label t, Label f) {
        if (exp instanceof Tree.CONST) {
            int value = ((Tree.CONST) exp).value;
            return new Tree.JUMP(value != 0 ? t : f);
        }
        return new Tree.CJUMP(
            Tree.CJUMP.NE, exp, new Tree.CONST(0), t, f
        );
    }
}
