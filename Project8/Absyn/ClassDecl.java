/**
 * COSC 4400 - Project #7
 * AST representation and visitor dispatch for compiler passes.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;
import java.util.LinkedList;

/**
 * Class Declaration Blocks
 */

public class ClassDecl extends Absyn
{
    public String name;
    public String parent;
    public LinkedList<VarDecl> fields;
    public LinkedList<MethodDecl> methods;
    public Types.CLASS checktype;
    public ClassDecl(String name, String parent, 
		     LinkedList<VarDecl> fields, LinkedList<MethodDecl> methods)
    {
		this.name = name;
		this.parent = parent;
		this.fields = fields;
		this.methods = methods;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}
