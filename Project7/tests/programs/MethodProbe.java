class Main {
    public static void main(String[] args) {
        int x = 7;
        x = 9;
    }
}

class Worker {
    public int copy(int p) {
        int local = 3;
        local = p;
        return local;
    }
}
