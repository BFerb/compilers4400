/**
 * COSC 4400 - Project #7
 * AST representation and visitor dispatch for compiler passes.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
package Absyn;

import java.util.LinkedList;

public class VoidDecl extends MethodDecl
{
    public VoidDecl(String name,
                    LinkedList<VarDecl> locals,
                    LinkedList<Stmt> stmts)
    {
        super(
            null,
            false,
            name,
            new LinkedList<Formal>(),
            locals,
            stmts,
            new IntegerLiteral(0)
        );
    }

    public void accept(Visitor v)
    {
        v.visit(this);
    }

    public Translate.Exp accept(TreeVisitor v) { return v.visit(this); }
}