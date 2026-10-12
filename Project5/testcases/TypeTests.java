class Main {
    public static void main(String[] args) {
    }
}

class Parent {
    int x;

    public int getX() {
        return x;
    }
}

class Child extends Parent {
    boolean flag;

    public int test(int a) {
        int b;
        Parent p;
        Child c;

        b = a + 1;
        flag = true;
        p = new Child();
        c = new Child();
        x = 10;

        if (flag) {
            b = x;
        }

        while (b < 10) {
            b = b + 1;
        }

        return b;
    }
}
