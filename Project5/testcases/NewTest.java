class NewTest {
    public static void main(String[] args) {
        Xinu.printint(new Solver().run(6));
    }
}

class Solver {
    int[] values;
    Worker worker;

    public int run(int n) {
        int i;
        int sum;
        boolean valid;

        values = new int[n];
        worker = new Worker();
        i = 0;
        sum = 0;
        valid = true;

        while (i < values.length) {
            values[i] = i + 1;
            sum = sum + worker.factorial(i);
            i = i + 1;
        }

        valid = this.isPositive(sum);

        if (valid && !(n == 0)) {
            sum = this.combine(sum, n);
        } else {
            sum = -1;
        }

        return sum;
    }

    public int combine(int x, int y) {
        int result;

        result = x + y * 2;
        return result;
    }

    public boolean isPositive(int x) {
        return x > 0;
    }
}

class Worker {
    public int factorial(int n) {
        int result;

        if (n < 2) {
            result = 1;
        } else {
            result = n * this.factorial(n - 1);
        }

        return result;
    }
}