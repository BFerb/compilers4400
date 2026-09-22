class Loops {
    public static void main (String[] a) {
        int x;

        x = 5;

        if (x < 10)
            Xinu.print(1);
        else
            Xinu.print(2);

        while (x < 10)
            x = x + 1;
    }
}