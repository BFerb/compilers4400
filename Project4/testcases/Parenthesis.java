class Parenthesis {
    public static void main (String[] a) {
        Xinu.print(1 + 2 * 3);
        Xinu.print((1 + 2) * 3);
        Xinu.print(20 - 6 / 2);
        Xinu.print((20 - 6) / 2);

        Xinu.print(1 < 2 && 3 < 4);
        Xinu.print(1 < 2 || 3 < 1);
        Xinu.print((1 < 2) && (3 < 1));
    }
}