/**
 * COSC 4400 - Project #7
 * Provides a translator regression test.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
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
