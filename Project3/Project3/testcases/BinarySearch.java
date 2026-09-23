// BinarySearch-style test case
class BinarySearch {
    public static void main (String[] a) {
        // Create an object and call a method.
        Xinu.printint(new BS().Start(20));
    }
}

class BS {
    int[] number;
    int size;

    public int Start(int sz) {
        int aux01;
        int aux02;

        // Initialize fields through method calls.
        aux01 = this.Init(sz);
        aux02 = this.Print();

        if (this.Search(8)) {
            Xinu.printint(1);
        } else {
            Xinu.printint(0);
        }

        return aux01 + aux02;
    }

    public int Init(int sz) {
        size = sz;
        return 1;
    }

    public int Print() {
        return size;
    }

    public boolean Search(int num) {
        return num < size;
    }
}