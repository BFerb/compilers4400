class Main {
    public static void main(String[] args) {
        Xinu.print("Testing");
        Xinu.printint(Xinu.readint());
        Xinu.println();
    }
}

class Box {
    int value;

    public int getValue() {
        return value;
    }

    public int add(int x) {
        return value + x;
    }
}

class Test {
    Box box;
    int[] nums;

    public int run() {
        int x;

        box = new Box();
        nums = new int[5];

        nums[0] = 10;
        box.value = nums[0];

        x = box.getValue();
        x = box.add(x);

        box = null;

        return x;
    }
}
