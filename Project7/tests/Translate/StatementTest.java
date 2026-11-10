package Translate;

import java.util.*;

public class StatementTest {
    static final Translate translator =
        new Translate(new Riscv.RiscvFrame());

    // Test-only statement containing a supplied IR instruction.
    static class Action extends Absyn.BlockStmt {
        final Tree.Stm code;
        Action(Tree.Stm code) {
            super(new LinkedList<Absyn.Stmt>());
            this.code = code;
        }
        public Exp accept(Absyn.TreeVisitor visitor) {
            return new Nx(code);
        }
    }

    // Records each condition check, then reads a changing Boolean value.
    static class Condition extends Absyn.IntegerLiteral {
        final Temp.Temp flag, probe;
        Condition(Temp.Temp flag, Temp.Temp probe) {
            super(0);
            this.flag = flag;
            this.probe = probe;
        }
        public Exp accept(Absyn.TreeVisitor visitor) {
            return new Ex(new Tree.ESEQ(
                store(probe, 1), new Tree.TEMP(flag)));
        }
    }

    static Tree.Stm store(Temp.Temp temp, int value) {
        return new Tree.MOVE(new Tree.TEMP(temp), new Tree.CONST(value));
    }

    static Absyn.BlockStmt block(Absyn.Stmt... statements) {
        return new Absyn.BlockStmt(
            new LinkedList<Absyn.Stmt>(Arrays.asList(statements)));
    }

    static BooleanTest.Machine machine(Temp.Temp... markers) {
        BooleanTest.Machine result = new BooleanTest.Machine();
        result.markers.addAll(Arrays.asList(markers));
        return result;
    }

    static void run(BooleanTest.Machine machine, Absyn.Stmt statement) {
        machine.run(statement.accept(translator).unNx());
    }

    static void trace(BooleanTest.Machine machine, Temp.Temp... expected) {
        if (!machine.trace.equals(Arrays.asList(expected)))
            throw new AssertionError("Wrong statement execution order");
    }

    public static void main(String[] args) {
        Temp.Temp a = new Temp.Temp();
        Temp.Temp b = new Temp.Temp();

        BooleanTest.Machine m = machine(a, b);
        run(m, block());
        trace(m);

        m = machine(a, b);
        run(m, block(new Action(store(a, 1)), new Action(store(b, 1))));
        trace(m, a, b);

        for (int value = 0; value <= 1; value++) {
            m = machine(a, b);
            run(m, new Absyn.IfStmt(new Absyn.IntegerLiteral(value),
                new Action(store(a, 1)), new Action(store(b, 1))));
            trace(m, value == 0 ? b : a);

            m = machine(a);
            run(m, new Absyn.IfStmt(new Absyn.IntegerLiteral(value),
                new Action(store(a, 1)), null));
            if (value == 0) trace(m);
            else trace(m, a);
        }

        for (int initial = 0; initial <= 1; initial++) {
            Temp.Temp flag = new Temp.Temp();
            Temp.Temp probe = new Temp.Temp();
            m = machine(probe, a);
            m.values.put(flag, initial);

            Absyn.Stmt body = block(
                new Action(store(a, 1)),
                new Action(store(flag, 0)));

            run(m, new Absyn.WhileStmt(new Condition(flag, probe), body));
            if (initial == 0) trace(m, probe);
            else trace(m, probe, a, probe);

            if (m.values.get(flag) != 0)
                throw new AssertionError("Loop failed to update its state");
        }

        System.out.println("PASS: 8 statement control-flow cases");
    }
}
