class Test {
    public static void main(String[] args) {
        int x;
        int y;

        x = 123;
        y = 077;

        if (x < 0xFF && y != 0) {
            Xinu.print("hello world");
            Xinu.println("x");
            Xinu.printint(x + y);
        } else {
            y = y - 1;
        }

        // line comment
        /* block
           comment */

        while (x > 0) {
            x = x - 1;
        }

        return;
    }
}
