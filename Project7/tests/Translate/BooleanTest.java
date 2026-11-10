package Translate;

import java.util.*;

public class BooleanTest {
    // A test operand whose generated code records its evaluation.
    static class Operand extends Absyn.IntegerLiteral {
        final Temp.Temp marker = new Temp.Temp();

        Operand(int value) { super(value); }

        public Exp accept(Absyn.TreeVisitor visitor) {
            return new Ex(new Tree.ESEQ(
                new Tree.MOVE(new Tree.TEMP(marker), new Tree.CONST(1)),
                new Tree.CONST(value)));
        }
    }

    // Interpreter for the IR subset exercised by these tests.
    static class Machine {
        final Map<Temp.Temp, Integer> values = new HashMap<>();
        final Set<Temp.Temp> markers = new HashSet<>();
        final List<Temp.Temp> trace = new ArrayList<>();

        int eval(Tree.Exp exp) {
            if (exp instanceof Tree.CONST)
                return ((Tree.CONST) exp).value;
            if (exp instanceof Tree.TEMP) {
                Integer value = values.get(((Tree.TEMP) exp).temp);
                if (value == null)
                    throw new AssertionError("Read of uninitialized temporary");
                return value;
            }
            if (exp instanceof Tree.ESEQ) {
                Tree.ESEQ sequence = (Tree.ESEQ) exp;
                run(sequence.stm);
                return eval(sequence.exp);
            }
            throw new AssertionError("Unsupported expression: " + exp);
        }

        void flatten(Tree.Stm stm, List<Tree.Stm> code) {
            if (stm == null) return;
            if (stm instanceof Tree.SEQ) {
                flatten(((Tree.SEQ) stm).left, code);
                flatten(((Tree.SEQ) stm).right, code);
            } else {
                code.add(stm);
            }
        }

        int target(Map<Temp.Label, Integer> labels, Temp.Label label) {
            Integer position = labels.get(label);
            if (position == null)
                throw new AssertionError("Undefined jump target: " + label);
            return position;
        }

        void run(Tree.Stm stm) {
            List<Tree.Stm> code = new ArrayList<>();
            flatten(stm, code);
            Map<Temp.Label, Integer> labels = new HashMap<>();
            for (int i = 0; i < code.size(); i++) {
                if (code.get(i) instanceof Tree.LABEL)
                    labels.put(((Tree.LABEL) code.get(i)).label, i);
            }

            int steps = 0;
            for (int pc = 0; pc < code.size();) {
                if (++steps > 1000)
                    throw new AssertionError("Possible infinite loop");
                Tree.Stm instruction = code.get(pc++);
                if (instruction instanceof Tree.LABEL) continue;

                if (instruction instanceof Tree.MOVE) {
                    Tree.MOVE move = (Tree.MOVE) instruction;
                    int value = eval(move.src);
                    Temp.Temp destination = ((Tree.TEMP) move.dst).temp;
                    values.put(destination, value);
                    if (markers.contains(destination)) trace.add(destination);
                } else if (instruction instanceof Tree.EXP) {
                    eval(((Tree.EXP) instruction).exp);
                } else if (instruction instanceof Tree.JUMP) {
                    Tree.NAME destination =
                        (Tree.NAME) ((Tree.JUMP) instruction).exp;
                    pc = target(labels, destination.label);
                } else if (instruction instanceof Tree.CJUMP) {
                    Tree.CJUMP jump = (Tree.CJUMP) instruction;
                    if (jump.relop != Tree.CJUMP.NE)
                        throw new AssertionError("Unexpected comparison");
                    boolean condition = eval(jump.left) != eval(jump.right);
                    pc = target(labels, condition ? jump.iftrue : jump.iffalse);
                } else {
                    throw new AssertionError("Unsupported statement");
                }
            }
        }
    }

    static void test(boolean or, int left, int right, int mode) {
        Operand a = new Operand(left);
        Operand b = new Operand(right);
        Absyn.Expr ast = or
            ? new Absyn.OrExpr(a, b)
            : new Absyn.AndExpr(a, b);

        final Exp translated =
            ast.accept(new Translate(new Riscv.RiscvFrame()));

        Machine machine = new Machine();
        machine.markers.add(a.marker);
        machine.markers.add(b.marker);

        int expected = (or
            ? left != 0 || right != 0
            : left != 0 && right != 0) ? 1 : 0;

        String name = (or ? "OR" : "AND") + "(" + left + "," + right
            + "), mode=" + mode;

        if (mode == 1) {
            machine.run(translated.unNx());
        } else {
            Exp expression = translated;
            if (mode == 2) {
                expression = new Cx() {
                    Tree.Stm unCx(Temp.Label t, Temp.Label f) {
                        return translated.unCx(t, f);
                    }
                };
            }
            int actual = machine.eval(expression.unEx());
            if (actual != expected)
                throw new AssertionError(name + ": wrong Boolean result");
        }

        List<Temp.Temp> expectedTrace = new ArrayList<>();
        expectedTrace.add(a.marker);
        if (or ? left == 0 : left != 0) expectedTrace.add(b.marker);
        if (!machine.trace.equals(expectedTrace))
            throw new AssertionError(name + ": wrong operand evaluation order");
    }

    public static void main(String[] args) {
        int count = 0;
        for (boolean or : new boolean[] {false, true})
            for (int left = 0; left <= 1; left++)
                for (int right = 0; right <= 1; right++)
                    for (int mode = 0; mode < 3; mode++) {
                        test(or, left, right, mode);
                        count++;
                    }
        System.out.println("PASS: " + count + " Boolean short-circuit cases");
    }
}
