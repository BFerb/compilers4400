class Main {
    public static void main(String[] args) {
        int[] values = new int[3];
        int[][] matrix = new int[2][];

        values[1] = 9;
        matrix[0] = new int[3];
        Xinu.printint(values[1]);
        Xinu.printint(values.length);
    }
}
