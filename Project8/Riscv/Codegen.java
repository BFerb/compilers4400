/**
 * COSC 4400 - Project #8
 * Selects RISC-V instructions for canonical MiniJava IR.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
package Riscv;

import Assem.Instr;
import Assem.OPER;
import Temp.Temp;
import Tree.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Codegen extends CodeVisitor {
    private final RiscvFrame frame;
    private final List<Instr> code = new ArrayList<Instr>();
    // An argument move can overwrite a register needed by a later operand.
    private Map<Temp, Temp> savedArguments = Collections.emptyMap();

    public Codegen(RiscvFrame frame) {
        this.frame = frame;
    }

    public List<Instr> select(List<Stm> statements) {
        for (Stm statement : statements) statement.accept(this);
        return code;
    }

    private static boolean fitsImmediate(long value) {
        return value >= -2048 && value <= 2047;
    }

    private void move(Temp destination, Temp source) {
        code.add(new Assem.MOVE("\tmv\t`d0,\t`s0", destination, source));
    }

    private void operation(String text, Temp destination, Temp... sources) {
        code.add(new OPER(text, destination == null ? null : new Temp[]{destination}, sources, null));
    }

    public void visit(Tree.MOVE node) {
        if (node.dst instanceof TEMP) {
            move(((TEMP) node.dst).temp, node.src.accept(this));
        } else if (node.dst instanceof MEM) {
            Address address = address(((MEM) node.dst).exp);
            store(node.src.accept(this), address);
        } else {
            throw new IllegalArgumentException("Invalid MOVE destination");
        }
    }

    public void visit(EXP node) {
        node.exp.accept(this);
    }

    public void visit(Tree.LABEL node) {
        code.add(new Assem.LABEL(node.label + ":", node.label));
    }

    public void visit(JUMP node) {
        if (node.exp instanceof NAME) {
            code.add(new OPER("\tj\t`j0", null, null, node.targets));
        } else {
            code.add(new OPER("\tjr\t`s0", null, new Temp[]{node.exp.accept(this)}, node.targets));
        }
    }

    public void visit(CJUMP node) {
        String branch;
        switch (node.relop) {
            case CJUMP.EQ:  branch = "beq"; break;
            case CJUMP.NE:  branch = "bne"; break;
            case CJUMP.LT:  branch = "blt"; break;
            case CJUMP.GE:  branch = "bge"; break;
            case CJUMP.GT:  branch = "bgt"; break;
            case CJUMP.LE:  branch = "ble"; break;
            case CJUMP.ULT: branch = "bltu"; break;
            case CJUMP.UGE: branch = "bgeu"; break;
            case CJUMP.UGT: branch = "bgtu"; break;
            case CJUMP.ULE: branch = "bleu"; break;
            default: throw new IllegalArgumentException("Unknown relation " + node.relop);
        }
        Temp[] sources = {node.left.accept(this), node.right.accept(this)};
        code.add(new OPER("\t" + branch + "\t`s0,\t`s1,\t`j0", null, sources,
                          Arrays.asList(node.iftrue, node.iffalse)));
    }

    public Temp visit(TEMP node) {
        Temp saved = savedArguments.get(node.temp);
        return saved == null ? node.temp : saved;
    }

    public Temp visit(CONST node) {
        if (node.value == 0) return RiscvFrame.ZERO;
        Temp result = new Temp();
        operation("\tli\t`d0,\t" + node.value, result);
        return result;
    }

    public Temp visit(NAME node) {
        Temp result = new Temp();
        operation("\tla\t`d0,\t" + node.label, result);
        return result;
    }

    private static class Address {
        final Temp base;
        final int offset;

        Address(Temp base, int offset) {
            this.base = base;
            this.offset = offset;
        }
    }

    private Address address(Exp expression) {
        if (expression instanceof BINOP) {
            BINOP op = (BINOP) expression;
            if (op.right instanceof CONST && (op.binop == BINOP.PLUS || op.binop == BINOP.MINUS)) {
                long offset = ((CONST) op.right).value;
                if (op.binop == BINOP.MINUS) offset = -offset;
                if (fitsImmediate(offset)) return new Address(op.left.accept(this), (int) offset);
            }
            if (op.binop == BINOP.PLUS && op.left instanceof CONST) {
                int offset = ((CONST) op.left).value;
                if (fitsImmediate(offset)) return new Address(op.right.accept(this), offset);
            }
        } else if (expression instanceof CONST && fitsImmediate(((CONST) expression).value)) {
            return new Address(RiscvFrame.ZERO, ((CONST) expression).value);
        }
        return new Address(expression.accept(this), 0);
    }

    public Temp visit(MEM node) {
        Temp result = new Temp();
        Address address = address(node.exp);
        operation("\tlw\t`d0,\t" + address.offset + "(`s0)", result, address.base);
        return result;
    }

    private void store(Temp value, Address address) {
        operation("\tsw\t`s0,\t" + address.offset + "(`s1)", null, value, address.base);
    }

    public Temp visit(BINOP node) {
        Temp result = new Temp();
        Exp left = node.left, right = node.right;
        if (node.binop == BINOP.PLUS && left instanceof CONST && !(right instanceof CONST)) {
            left = node.right;
            right = node.left;
        }
        if (right instanceof CONST) {
            int constant = ((CONST) right).value;
            long immediate = node.binop == BINOP.MINUS ? -(long) constant : constant;
            String opcode = null;
            if (fitsImmediate(immediate)) {
                switch (node.binop) {
                    case BINOP.PLUS:
                    case BINOP.MINUS: opcode = "addi"; break;
                    case BINOP.BITAND: opcode = "andi"; break;
                    case BINOP.BITOR: opcode = "ori"; break;
                    case BINOP.BITXOR: opcode = "xori"; break;
                }
            }
            if (constant >= 0 && constant < 32) {
                switch (node.binop) {
                    case BINOP.LSHIFT: opcode = "slli"; break;
                    case BINOP.RSHIFT: opcode = "srli"; break;
                    case BINOP.ARSHIFT: opcode = "srai"; break;
                }
            }
            if (opcode != null) {
                operation("\t" + opcode + "\t`d0,\t`s0,\t" + immediate, result, left.accept(this));
                return result;
            }
        }

        Temp a = left.accept(this);
        Temp b = right.accept(this);
        String opcode;
        switch (node.binop) {
            case BINOP.PLUS:   opcode = "add"; break;
            case BINOP.MINUS:  opcode = "sub"; break;
            case BINOP.MUL:    opcode = "mul"; break;
            case BINOP.DIV:    opcode = "div"; break;
            case BINOP.AND:
            case BINOP.BITAND: opcode = "and"; break;
            case BINOP.OR:
            case BINOP.BITOR:  opcode = "or"; break;
            case BINOP.BITXOR: opcode = "xor"; break;
            case BINOP.LSHIFT: opcode = "sll"; break;
            case BINOP.RSHIFT: opcode = "srl"; break;
            case BINOP.ARSHIFT: opcode = "sra"; break;
            default: throw new IllegalArgumentException("Unknown binary operator " + node.binop);
        }
        operation("\t" + opcode + "\t`d0,\t`s0,\t`s1", result, a, b);
        return result;
    }

    private static boolean reads(Exp exp, Temp register) {
        if (exp instanceof TEMP) return ((TEMP) exp).temp == register;
        for (Exp child : exp.kids()) {
            if (reads(child, register)) return true;
        }
        return false;
    }

    public Temp visit(CALL node) {
        List<Exp> args = node.args == null ? Collections.<Exp>emptyList() : node.args;
        Temp[] registers = RiscvFrame.argRegs;
        int passed = Math.min(registers.length, args.size());
        List<Temp> stack = new ArrayList<Temp>();
        List<Temp> uses = new ArrayList<Temp>();
        savedArguments = new HashMap<Temp, Temp>();
        for (int i = 0; i < args.size(); i++) {
            Temp value = args.get(i).accept(this);
            if (i >= passed) {
                stack.add(value);
                continue;
            }
            Temp register = registers[i];
            boolean neededLater = reads(node.func, register);
            for (int j = i + 1; !neededLater && j < args.size(); j++) {
                neededLater = reads(args.get(j), register);
            }
            if (neededLater && value != register) {
                Temp saved = new Temp();
                move(saved, register);
                savedArguments.put(register, saved);
            }
            move(register, value);
            uses.add(register);
        }
        String instruction;
        if (node.func instanceof NAME) {
            instruction = "\tcall\t" + ((NAME) node.func).label;
        } else {
            uses.add(0, node.func.accept(this));
            instruction = "\tjalr\t`s0";
        }
        savedArguments = Collections.emptyMap();
        int offset = 0;
        for (Temp value : stack) {
            store(value, address(new BINOP(BINOP.PLUS, new TEMP(RiscvFrame.SP), new CONST(offset))));
            offset += frame.wordSize();
        }
        if (!stack.isEmpty()) uses.add(RiscvFrame.SP);
        frame.maxArgOffset = Math.max(frame.maxArgOffset, offset);
        code.add(new OPER(instruction, RiscvFrame.calldefs.clone(), uses.toArray(new Temp[0]), null));
        code.add(new OPER("\t// Call sink", null, RiscvFrame.calldefs.clone(), null));
        return frame.RV();
    }

    public void visit(SEQ node) {
        throw new IllegalArgumentException("SEQ reached instruction selection before linearization");
    }

    public Temp visit(ESEQ node) {
        throw new IllegalArgumentException("ESEQ reached instruction selection before linearization");
    }
}
