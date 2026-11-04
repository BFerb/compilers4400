/**
 * COSC 4400 - Project #7
 * AST representation and visitor dispatch for compiler passes.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 2013, Marquette University.  All rights reserved. */
package Absyn;
import java.util.LinkedList;

/**
 * Xinu Statements.
 */

public class XinuCallStmt extends Stmt
{
    public String method;
    public LinkedList<Expr> args;
    public XinuCallStmt(String method, LinkedList<Expr> args)
    {
		this.method = method;
		this.args   = args;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}
