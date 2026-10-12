class ArrayExpressions {
    public static void main (String[] a) {
        int[] numbers;

        numbers = new int[10];

        numbers[1 + 2] = 5;
        numbers[3 * 2] = numbers[1] + numbers[2];

        Xinu.print(numbers[1 + 2]);
        Xinu.print(numbers.length);
    }
}