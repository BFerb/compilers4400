class HardExpressions {
    public static void main (String[] a) {
        Xinu.print(1 + 2 * 3 - 4);
        Xinu.print((1 + 2) * 3);
        Xinu.print(1 < 2 && 3 < 4);
        Xinu.print(1 < 2 || 3 < 1);
        Xinu.print(!(1 == 2));
        Xinu.print((1 + 2) * (3 - 4) / 5);
        Xinu.print(1 + 3 - 1 * 8 / 2 || 1 + 2 * 3 - 4);
    }
}