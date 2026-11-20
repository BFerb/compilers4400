/**
 * COSC 4400 - Project #7
 * AST representation and visitor dispatch for compiler passes.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;
import java.util.AbstractList;

/**
 * Goal 	::= 	MainClass ( ClassDeclaration )* <EOF>
 */

public class Program extends Absyn
{
    public AbstractList<ClassDecl> classes;

    public Program(AbstractList<ClassDecl> classes)
    {
		this.classes = classes;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}
