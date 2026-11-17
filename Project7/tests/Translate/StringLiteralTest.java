/**
 * COSC 4400 - Project #7
 * Provides a translator regression test.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
package Translate;

public class StringLiteralTest {
    static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }

    static void literal(String token, String value) {
        Translate translator = new Translate(new Riscv.RiscvFrame());
        Tree.NAME translated = (Tree.NAME)
            new Absyn.StringLiteral(token).accept(translator).unEx();
        DataFrag data = (DataFrag) translator.results().get(0);
        String expected = translator.frame.string(translated.label, value);
        check(data.data.equals(expected), "Incorrect string value for " + token);
    }

    public static void main(String[] args) {
        literal("\"Number of queens? \"", "Number of queens? ");
        literal("\"\"", "");
        literal("\"a\\nb\\t\\101\\\\\"", "a\nb\tA\\");
        literal("\"\\377\\400\"", "\u00ff 0");
        literal("\"\\\"\\'\"", "\"'");
        Translate translator = new Translate(new Riscv.RiscvFrame());
        Tree.NAME first = (Tree.NAME)
            new Absyn.StringLiteral("\"A\"").accept(translator).unEx();
        Tree.NAME second = (Tree.NAME)
            new Absyn.StringLiteral("\"\\101\"").accept(translator).unEx();
        check(first.label == second.label, "Equivalent string values were not shared");
        check(translator.results().size() == 1, "Duplicate string data emitted");
        System.out.println("PASS: string quotes, escapes, and interning");
    }
}
