/**
 * COSC 4400 - Project #8
 * Selects instructions for IR statements and computes expression results.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
package Tree;
import Temp.Temp;

public abstract class CodeVisitor {
    public abstract void visit(SEQ node);
    public abstract void visit(LABEL node);
    public abstract void visit(JUMP node);
    public abstract void visit(CJUMP node);
    public abstract void visit(MOVE node);
    public abstract void visit(EXP node);

    public abstract Temp visit(BINOP node);
    public abstract Temp visit(MEM node);
    public abstract Temp visit(TEMP node);
    public abstract Temp visit(ESEQ node);
    public abstract Temp visit(NAME node);
    public abstract Temp visit(CONST node);
    public abstract Temp visit(CALL node);
}
