class NestedControl {
    public static void main(String[] a) {
        Xinu.printint(new Test().Run(10));
    }
}

class Test {
    public int Run(int n) {
        int x;

        x = 0;

        while (x < n) {
            if (x < 5) {
                x = x + 1;
            } else {
                x = x + 2;
            }
        }

        return x;
    }
}