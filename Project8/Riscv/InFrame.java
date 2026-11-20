/**
 * COSC 4400 - Project #7
 * Implements RISC-V frame layout and runtime conventions.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
package Riscv;

/**
 * InFrame represents formal variables or local variables that may be stored
 *  in the activation record ("frame").
 * See MCIJ2e Section 6.2.
 */
public class InFrame extends Frame.Access {
    int offset;

    public InFrame(int o) {
	offset = o;
    }

    public Tree.Exp exp(Tree.Exp fp) {
	return new Tree.MEM(new Tree.BINOP(Tree.BINOP.PLUS, fp, new Tree.CONST(offset)));
    }

    public Tree.Exp exp(Frame.Frame f) {
	return new Tree.MEM(new Tree.BINOP(Tree.BINOP.PLUS, new Tree.TEMP(f.FP()),
					   new Tree.CONST(offset)));
    }

    public String toString() {
	Integer offset = new Integer(this.offset);
	return offset.toString();
    }
}
