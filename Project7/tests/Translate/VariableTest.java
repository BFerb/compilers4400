package Translate;

public class VariableTest {
    static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        Frame.Frame frame = new Riscv.RiscvFrame().newFrame(
            Symbol.Symbol.symbol("variable_test"));
        Translate translator = new Translate(frame);
        BooleanTest.Machine machine = new BooleanTest.Machine();

        Absyn.IdentifierExpr x = new Absyn.IdentifierExpr("x");
        Absyn.VarDecl local = new Absyn.VarDecl(
            null, "x", new Absyn.IntegerLiteral(7));

        machine.run(local.accept(translator).unNx());
        check(machine.eval(x.accept(translator).unEx()) == 7,
              "Local initializer did not store 7");

        Absyn.AssignStmt assignment = new Absyn.AssignStmt(
            x, new Absyn.IntegerLiteral(9));
        machine.run(assignment.accept(translator).unNx());
        check(machine.eval(x.accept(translator).unEx()) == 9,
              "Assignment did not replace the local value");

        new Absyn.Formal(null, "p").accept(translator);
        check(frame.formals.size() == 1, "Parameter was not allocated");
        check(frame.actuals.size() == 1, "Incoming argument location missing");

        Absyn.IdentifierExpr p = new Absyn.IdentifierExpr("p");
        machine.run(new Absyn.AssignStmt(
            p, new Absyn.IntegerLiteral(42)).accept(translator).unNx());

        check(machine.eval(p.accept(translator).unEx()) == 42,
              "Parameter lookup did not use its allocated location");
        check(machine.eval(x.accept(translator).unEx()) == 9,
              "Parameter storage overlaps local storage");

        System.out.println("PASS: variable initialization, assignment, and storage");
    }
}
