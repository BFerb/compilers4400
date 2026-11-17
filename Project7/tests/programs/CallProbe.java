/**
 * COSC 4400 - Project #7
 * Provides a translator regression test.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
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
