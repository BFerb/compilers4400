class Main {
    public static void main(String[] args) {
        Worker w = new Worker();
        w.first = 4;
        w.second = 9;
        Xinu.printint(w.first);
        Xinu.printint(w.second);
    }
}

class Worker {
    int first;
    int second;
}
