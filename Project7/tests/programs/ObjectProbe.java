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
