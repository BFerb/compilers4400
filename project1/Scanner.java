/**
 * COSC 4400 - Project #1
 * Implements a scanner for the COSC 4400 MiniJava language.
 *
 * @authors [Ben Ferber], [Nick Grons]
 * Instructor [Dennis Brylow]
 * TA-BOT:MAILTO [benjamin.ferber@marquette.edu], [nicholas.grons@marquette.edu]
 */

import java.io.IOException;
import java.io.InputStreamReader;

public class Scanner {

    private static final int START = 0;
    private static final int IDENTIFIER = 1;
    private static final int DECIMAL = 2;
    private static final int LEADING_ZERO = 3;
    private static final int OCTAL = 4;
    private static final int HEX_PREFIX = 5;
    private static final int HEX = 6;

    private static final int LETTER = 0;
    private static final int HEX_LETTER = 1;
    private static final int X_CHAR = 2;
    private static final int ZERO = 3;
    private static final int DIGIT_1_7 = 4;
    private static final int DIGIT_8_9 = 5;
    private static final int UNDERSCORE = 6;
    private static final int DELIMITER = 7;
    private static final int OTHER = 8;

    private static final int INVALID = -1;
    private static final int ACCEPT = -2;

    private static final int NUM_STATES = 7;
    private static final int NUM_CLASSES = 9;

    private static final int[][] transition =
        new int[NUM_STATES][NUM_CLASSES];

    private static final String[][] keywords = {
        {"class", "CLASS"},
        {"public", "PUBLIC"},
        {"static", "STATIC"},
        {"void", "VOID"},
        {"main", "MAIN"},
        {"String", "STRING"},
        {"extends", "EXTENDS"},
        {"return", "RETURN"},
        {"int", "INT"},
        {"boolean", "BOOLEAN"},
        {"if", "IF"},
        {"else", "ELSE"},
        {"while", "WHILE"},
        {"true", "TRUE"},
        {"false", "FALSE"},
        {"new", "NEW"},
        {"this", "THIS"},
        {"length", "LENGTH"},

        {"Xinu.print", "PRINT"},
        {"Xinu.println", "PRINTLN"},
        {"Xinu.printint", "PRINTINT"},
        {"Xinu.readint", "READINT"}
    };

    private static String input;
    private static int pos;

    private static void initializeTransitions() {

        for (int state = 0; state < NUM_STATES; state++) {
            for (int character = 0;
                 character < NUM_CLASSES;
                 character++) {

                transition[state][character] = INVALID;
            }
        }

        transition[START][LETTER] = IDENTIFIER;
        transition[START][HEX_LETTER] = IDENTIFIER;
        transition[START][X_CHAR] = IDENTIFIER;

        transition[START][ZERO] = LEADING_ZERO;

        transition[START][DIGIT_1_7] = DECIMAL;
        transition[START][DIGIT_8_9] = DECIMAL;

        transition[IDENTIFIER][LETTER] = IDENTIFIER;
        transition[IDENTIFIER][HEX_LETTER] = IDENTIFIER;
        transition[IDENTIFIER][X_CHAR] = IDENTIFIER;

        transition[IDENTIFIER][ZERO] = IDENTIFIER;
        transition[IDENTIFIER][DIGIT_1_7] = IDENTIFIER;
        transition[IDENTIFIER][DIGIT_8_9] = IDENTIFIER;

        transition[IDENTIFIER][UNDERSCORE] = IDENTIFIER;

        transition[IDENTIFIER][DELIMITER] = ACCEPT;
        transition[IDENTIFIER][OTHER] = INVALID;

        transition[DECIMAL][ZERO] = DECIMAL;
        transition[DECIMAL][DIGIT_1_7] = DECIMAL;
        transition[DECIMAL][DIGIT_8_9] = DECIMAL;

        transition[DECIMAL][DELIMITER] = ACCEPT;

        transition[DECIMAL][LETTER] = INVALID;
        transition[DECIMAL][HEX_LETTER] = INVALID;
        transition[DECIMAL][X_CHAR] = INVALID;
        transition[DECIMAL][UNDERSCORE] = INVALID;
        transition[DECIMAL][OTHER] = INVALID;

        transition[LEADING_ZERO][ZERO] = OCTAL;
        transition[LEADING_ZERO][DIGIT_1_7] = OCTAL;

        transition[LEADING_ZERO][X_CHAR] = HEX_PREFIX;

        transition[LEADING_ZERO][DELIMITER] = ACCEPT;

        transition[LEADING_ZERO][LETTER] = INVALID;
        transition[LEADING_ZERO][HEX_LETTER] = INVALID;
        transition[LEADING_ZERO][DIGIT_8_9] = INVALID;
        transition[LEADING_ZERO][UNDERSCORE] = INVALID;
        transition[LEADING_ZERO][OTHER] = INVALID;

        transition[OCTAL][ZERO] = OCTAL;
        transition[OCTAL][DIGIT_1_7] = OCTAL;

        transition[OCTAL][DELIMITER] = ACCEPT;

        transition[OCTAL][LETTER] = INVALID;
        transition[OCTAL][HEX_LETTER] = INVALID;
        transition[OCTAL][X_CHAR] = INVALID;
        transition[OCTAL][DIGIT_8_9] = INVALID;
        transition[OCTAL][UNDERSCORE] = INVALID;
        transition[OCTAL][OTHER] = INVALID;

        transition[HEX_PREFIX][ZERO] = HEX;
        transition[HEX_PREFIX][DIGIT_1_7] = HEX;
        transition[HEX_PREFIX][DIGIT_8_9] = HEX;
        transition[HEX_PREFIX][HEX_LETTER] = HEX;

        transition[HEX_PREFIX][DELIMITER] = ACCEPT;

        transition[HEX_PREFIX][LETTER] = INVALID;
        transition[HEX_PREFIX][X_CHAR] = INVALID;
        transition[HEX_PREFIX][UNDERSCORE] = INVALID;
        transition[HEX_PREFIX][OTHER] = INVALID;

        transition[HEX][ZERO] = HEX;
        transition[HEX][DIGIT_1_7] = HEX;
        transition[HEX][DIGIT_8_9] = HEX;
        transition[HEX][HEX_LETTER] = HEX;

        transition[HEX][DELIMITER] = ACCEPT;

        transition[HEX][LETTER] = INVALID;
        transition[HEX][X_CHAR] = INVALID;
        transition[HEX][UNDERSCORE] = INVALID;
        transition[HEX][OTHER] = INVALID;
    }

    private static int getCharClass(char c) {

        if (c == 'x' || c == 'X') {
            return X_CHAR;
        }

        if ((c >= 'a' && c <= 'f') ||
            (c >= 'A' && c <= 'F')) {

            return HEX_LETTER;
        }

        if (isAsciiLetter(c)) {
            return LETTER;
        }

        if (c == '0') {
            return ZERO;
        }

        if (c >= '1' && c <= '7') {
            return DIGIT_1_7;
        }

        if (c == '8' || c == '9') {
            return DIGIT_8_9;
        }

        if (c == '_') {
            return UNDERSCORE;
        }

        if (isDelimiter(c)) {
            return DELIMITER;
        }

        return OTHER;
    }

    private static boolean isDelimiter(char c) {

        if (Character.isWhitespace(c)) {
            return true;
        }

        return "{}()[];,.=!+-*/&|<>^~\"".indexOf(c) >= 0;
    }

    private static String keywordToken(String lexeme) {

        for (int i = 0; i < keywords.length; i++) {

            if (keywords[i][0].equals(lexeme)) {
                return keywords[i][1];
            }
        }

        return null;
    }

    private static boolean startsWith(String text) {
        return input.startsWith(text, pos);
    }

    private static boolean isIdentifierPart(char c) {
        return isAsciiLetterOrDigit(c) || c == '_';
    }

    private static boolean isAsciiLetter(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z');
    }

    private static boolean isAsciiDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isAsciiLetterOrDigit(char c) {
        return isAsciiLetter(c) || isAsciiDigit(c);
    }

    private static void scanIdentifier() {

    int start = pos;
    int state = START;

    while (pos < input.length()) {

        char c = input.charAt(pos);
        int characterClass = getCharClass(c);

        int nextState =
            transition[state][characterClass];

        if (nextState == ACCEPT) {
            break;
        }

        if (nextState == INVALID) {

            pos++;

            while (pos < input.length()) {

                char bad = input.charAt(pos);

                if (Character.isWhitespace(bad) ||
                    isDelimiter(bad)) {

                    break;
                }

                pos++;
            }

            System.out.println("Illegal token.");
            return;
        }

        state = nextState;
        pos++;
    }

    String lexeme =
        input.substring(start, pos);

    if (lexeme.equals("Xinu") &&
        pos < input.length() &&
        input.charAt(pos) == '.') {

        String[] suffixes = {
            ".println",
            ".printint",
            ".readint",
            ".print"
        };

        for (int i = 0; i < suffixes.length; i++) {

            String suffix = suffixes[i];

            if (startsWith(suffix)) {

                int end = pos + suffix.length();

                if (end == input.length() ||
                    !isIdentifierPart(
                        input.charAt(end))) {

                    lexeme += suffix;
                    pos = end;
                    break;
                }
            }
        }
    }

    String token = keywordToken(lexeme);

    if (token != null) {
        System.out.println(token);
    }
    else {
        System.out.println(
            "ID(" + lexeme + ")");
    }
}

    private static void consumeBadNumberTail() {

        while (pos < input.length()) {

            char c = input.charAt(pos);

            if (isAsciiLetterOrDigit(c) ||
                c == '_') {

                pos++;
            }
            else {
                break;
            }
        }
    }

    private static void scanNumber() {

        int start = pos;
        int state = START;

        boolean error = false;
        String errorMessage = null;

        while (pos < input.length()) {

            char c = input.charAt(pos);

            int characterClass =
                getCharClass(c);

            int nextState =
                transition[state][characterClass];

            if (nextState == ACCEPT) {
                break;
            }

            if (nextState == INVALID) {

                error = true;

                if (state == OCTAL) {

                    errorMessage =
                        "Invalid character in octal number.";
                }
                else if (state == HEX_PREFIX ||
                         state == HEX) {

                    errorMessage =
                        "Invalid character in hex number.";
                }
                else {

                    errorMessage =
                        "Invalid character in number.";
                }

                pos++;

                consumeBadNumberTail();

                break;
            }

            state = nextState;
            pos++;
        }

        if (error) {

            System.out.println(errorMessage);
            return;
        }

        String lexeme =
            input.substring(start, pos);

        if (state == OCTAL) {

            System.out.println(
                "OCTAL_LITERAL(" +
                lexeme + ")");
        }
        else if (state == HEX_PREFIX ||
                 state == HEX) {

            System.out.println(
                "HEXADECIMAL_LITERAL(" +
                lexeme + ")");
        }
        else {

            System.out.println(
                "INTEGER_LITERAL(" +
                lexeme + ")");
        }
    }

    private static void scanString() {

        pos++;

        int start = pos;

        while (pos < input.length()) {

            char c = input.charAt(pos);

            if (c == '"') {

                String value =
                    input.substring(start, pos);

                System.out.println(
                    "STRING_LITERAL(" +
                    value + ")");

                pos++;
                return;
            }

            if (c == '\n' || c == '\r') {

                System.out.println(
                    "String not terminated at end of line.");

                if (c == '\r' &&
                    pos + 1 < input.length() &&
                    input.charAt(pos + 1) == '\n') {

                    pos++;
                }

                pos++;
                return;
            }

            pos++;
        }

        System.out.println(
            "String not terminated at end of line.");
    }

    private static void scanSlashOrComment() {

        if (pos + 1 >= input.length()) {

            System.out.println("FORWARDSLASH");
            pos++;
            return;
        }

        char next = input.charAt(pos + 1);

        if (next == '/') {

            pos += 2;

            while (pos < input.length() &&
                   input.charAt(pos) != '\n' &&
                   input.charAt(pos) != '\r') {

                pos++;
            }

            return;
        }

        if (next == '*') {

            pos += 2;

            while (pos < input.length()) {

                if (input.charAt(pos) == '*' &&
                    pos + 1 < input.length() &&
                    input.charAt(pos + 1) == '/') {

                    pos += 2;
                    return;
                }

                pos++;
            }

            System.out.println(
                "Comment not terminated at end of input.");

            return;
        }

        System.out.println("FORWARDSLASH");
        pos++;
    }

    private static void scanPossibleDouble(
        char secondCharacter,
        String doubleToken,
        String singleToken) {

        if (pos + 1 < input.length() &&
            input.charAt(pos + 1) ==
                secondCharacter) {

            System.out.println(doubleToken);
            pos += 2;
        }
        else {

            System.out.println(singleToken);
            pos++;
        }
    }

    private static void scanIllegalToken() {

        pos++;

        while (pos < input.length()) {

            char c = input.charAt(pos);

            if (Character.isWhitespace(c) ||
                isDelimiter(c)) {

                break;
            }

            pos++;
        }

        System.out.println("Illegal token.");
    }

    private static void scanOperatorOrPunctuation() {

        char c = input.charAt(pos);

        switch (c) {

            case '{':
                System.out.println("LBRACE");
                pos++;
                break;

            case '}':
                System.out.println("RBRACE");
                pos++;
                break;

            case '(':
                System.out.println("LPAREN");
                pos++;
                break;

            case ')':
                System.out.println("RPAREN");
                pos++;
                break;

            case '[':
                System.out.println("LSQUARE");
                pos++;
                break;

            case ']':
                System.out.println("RSQUARE");
                pos++;
                break;

            case ';':
                System.out.println("SEMICOLON");
                pos++;
                break;

            case ',':
                System.out.println("COMMA");
                pos++;
                break;

            case '.':
                System.out.println("PERIOD");
                pos++;
                break;

            case '=':
                scanPossibleDouble(
                    '=',
                    "EQUAL",
                    "ASSIGN");
                break;

            case '!':
                scanPossibleDouble(
                    '=',
                    "NOTEQUAL",
                    "BANG");
                break;

            case '+':
                System.out.println("PLUS");
                pos++;
                break;

            case '-':
                System.out.println("MINUS");
                pos++;
                break;

            case '*':
                System.out.println("STAR");
                pos++;
                break;

            case '/':
                scanSlashOrComment();
                break;

            case '&':
                scanPossibleDouble(
                    '&',
                    "AND",
                    "BWAND");
                break;

            case '|':
                scanPossibleDouble(
                    '|',
                    "OR",
                    "BWOR");
                break;

            case '<':
                System.out.println("LESSTHAN");
                pos++;
                break;

            case '>':
                System.out.println("GREATERTHAN");
                pos++;
                break;

            case '^':
                System.out.println("XOR");
                pos++;
                break;

            case '~':
                System.out.println("COMP");
                pos++;
                break;

            default:
                scanIllegalToken();
                break;
        }
    }

    private static void readInput()
        throws IOException {

        InputStreamReader reader =
            new InputStreamReader(System.in);

        StringBuilder builder =
            new StringBuilder();

        int c;

        while ((c = reader.read()) != -1) {
            builder.append((char)c);
        }

        input = builder.toString();
    }

    public static void main(String[] args)
        throws IOException {

        initializeTransitions();
        readInput();

        pos = 0;

        while (pos < input.length()) {

            char c = input.charAt(pos);

            if (Character.isWhitespace(c)) {

                pos++;
            }

            else if (isAsciiLetter(c)) {

                scanIdentifier();
            }

            else if (isAsciiDigit(c)) {

                scanNumber();
            }

            else if (c == '"') {

                scanString();
            }

            else {

                scanOperatorOrPunctuation();
            }
        }

        System.out.println("EOF");
    }
}
