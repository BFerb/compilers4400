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
    boolean b;

    public int getY(int a) {
        return a;
    }
}