/**
 * COSC 4400 - Project #7
 * Provides a translator regression test.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
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
