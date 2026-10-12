class Arrays {
    public static void main (String[] a) {
        int[] x;

        x = new int[5];
        x[0] = 10;
        x[1] = 20;
        x[2] = x[0] + x[1];

        Xinu.print(x[0]);
        Xinu.print(x[2]);
        Xinu.print(x.length);
    }
}