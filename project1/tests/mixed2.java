class Weird123 {
    int abc_;
    int a__b;

    public static void main(String[] args) {
        abc_ = 0;
        a__b = 0123;

        if (abc_ == a__b || abc_ != 0x1a2b) {
            Xinu.readint();
        }

        abc_ = abc_ & a__b;
        abc_ = abc_ | a__b;
        abc_ = abc_ ^ a__b;
        abc_ = ~abc_;

        Xinu.print("");
        Xinu.println("a+b=c");
    }
}
