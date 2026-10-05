class Blocks {
    public static void main (String[] a) {
        int x;

        x = 1;

        {
            x = 2;
            Xinu.print(x);
        }

        {
            x = x + 3;
            Xinu.print(x);
        }
    }
}