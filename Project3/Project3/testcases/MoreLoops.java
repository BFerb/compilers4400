class MoreLoops {
    public static void main (String[] a) {
        int x;

        x = 0;

        while (x < 10) {
            if (x < 5) {
                Xinu.print(x);
            } else {
                Xinu.print(x + 10);
            }

            x = x + 1;
        }
    }
}