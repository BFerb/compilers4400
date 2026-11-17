/**
 * COSC 4400 - Project #7
 * Translates conditional expressions into IR branches and joins.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved. */
package Translate;

import Temp.Temp;
import Temp.Label;

public class IfThenElseExp extends Exp {
    Exp cond, trueExp, falseExp;

    IfThenElseExp(Exp cExp, Exp tExp, Exp fExp) {
        cond = cExp;
        trueExp = tExp;
        falseExp = fExp;
    }

    private static Tree.Stm sequence(Tree.Stm... statements) {
        Tree.Stm result = null;
        for (Tree.Stm statement : statements) {
            if (statement != null) {
                result = result == null
                    ? statement : new Tree.SEQ(result, statement);
            }
        }
        return result;
    }

    Tree.Stm unCx(Label tt, Label ff) {
        if (trueExp instanceof Ex && falseExp instanceof Ex) {
            Tree.Exp tValue = ((Ex) trueExp).exp;
            Tree.Exp fValue = ((Ex) falseExp).exp;
            if (tValue instanceof Tree.CONST && fValue instanceof Tree.CONST) {
                Label tTarget = ((Tree.CONST) tValue).value != 0 ? tt : ff;
                Label fTarget = ((Tree.CONST) fValue).value != 0 ? tt : ff;
                return cond.unCx(tTarget, fTarget);
            }
        }
        if (falseExp instanceof Ex && ((Ex) falseExp).exp instanceof Tree.CONST) {
            Label next = new Label();
            Label target = ((Tree.CONST) ((Ex) falseExp).exp).value != 0 ? tt : ff;
            return sequence(cond.unCx(next, target),
                sequence(new Tree.LABEL(next), trueExp.unCx(tt, ff)));
        }
        if (trueExp instanceof Ex && ((Ex) trueExp).exp instanceof Tree.CONST) {
            Label next = new Label();
            Label target = ((Tree.CONST) ((Ex) trueExp).exp).value != 0 ? tt : ff;
            return sequence(cond.unCx(target, next),
                sequence(new Tree.LABEL(next), falseExp.unCx(tt, ff)));
        }
        Label t = new Label();
        Label f = new Label();

        return sequence(
            cond.unCx(t, f),
            new Tree.LABEL(t),
            trueExp.unCx(tt, ff),
            new Tree.LABEL(f),
            falseExp.unCx(tt, ff)
        );
    }

    Tree.Exp unEx() {
        Tree.Exp tExp = trueExp.unEx();
        Tree.Exp fExp = falseExp.unEx();
        Temp result = new Temp();
        Label t = new Label();
        Label f = new Label();
        Label join = new Label();

        return new Tree.ESEQ(
            sequence(
                cond.unCx(t, f),
                new Tree.LABEL(t),
                new Tree.MOVE(new Tree.TEMP(result), tExp),
                new Tree.JUMP(join),
                new Tree.LABEL(f),
                new Tree.MOVE(new Tree.TEMP(result), fExp),
                new Tree.JUMP(join),
                new Tree.LABEL(join)
            ),
            new Tree.TEMP(result)
        );
    }

    Tree.Stm unNx() {
        Tree.Stm tStm = trueExp.unNx();
        Tree.Stm fStm = falseExp == null ? null : falseExp.unNx();
        Label t = new Label();
        Label f = new Label();
        if (fStm == null) {
            return sequence(cond.unCx(t, f),
                sequence(new Tree.LABEL(t), tStm, new Tree.JUMP(f)),
                new Tree.LABEL(f));
        }
        Label join = new Label();

        return sequence(
            cond.unCx(t, f),
            sequence(
                sequence(new Tree.LABEL(t), tStm, new Tree.JUMP(join)),
                sequence(new Tree.LABEL(f), fStm, new Tree.JUMP(join))
            ),
            new Tree.LABEL(join)
        );
    }
}
