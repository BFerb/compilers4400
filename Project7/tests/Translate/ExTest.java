package Translate;

public class ExTest {
    static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        Temp.Label yes = new Temp.Label();
        Temp.Label no = new Temp.Label();

        for (int value : new int[] {0, 1, -7}) {
            Ex ex = new Ex(new Tree.CONST(value));
            check(ex.unNx() == null, "Discard standalone constant");
            Tree.JUMP jump = (Tree.JUMP) ex.unCx(yes, no);
            check(jump.targets.getFirst() == (value == 0 ? no : yes),
                  "Wrong branch for " + value);
        }

        Tree.Exp value = new Tree.TEMP(new Temp.Temp());
        Ex ex = new Ex(value);
        check(ex.unEx() == value, "Preserve value expression");
        check(((Tree.EXP) ex.unNx()).exp == value,
              "Preserve evaluation when discarding result");

        Tree.CJUMP jump = (Tree.CJUMP) ex.unCx(yes, no);
        check(jump.relop == Tree.CJUMP.NE, "Use not-equal comparison");
        check(jump.left == value, "Preserve condition");
        check(((Tree.CONST) jump.right).value == 0, "Compare against zero");
        check(jump.iftrue == yes && jump.iffalse == no,
              "Correct branch destinations");

        System.out.println("PASS: Ex conversions");
    }
}
