class Main {
    public static void main(String[] args) {
        Worker w = new Worker();
        Xinu.printint(w.sum(4, 9));
        Xinu.printint(new Worker().first());
    }
}

class Worker {
    public int first() {
        return 1;
    }

    public int sum(int a, int b) {
        return a + b;
    }
}
