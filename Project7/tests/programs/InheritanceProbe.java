class Main {
    public static void main(String[] args) {
        Parent p = new Child();
        Xinu.printint(p.update(5));
        Xinu.printint(p.read());
    }
}

class Parent {
    int saved;

    public int update(int value) {
        saved = value;
        return saved;
    }

    public int read() {
        return saved;
    }
}

class Child extends Parent {
    int extra;

    public int update(int value) {
        saved = value;
        extra = value + 1;
        return saved + extra;
    }
}
