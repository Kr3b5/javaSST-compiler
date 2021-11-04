package Scanner;

public class Scanner {
    /*
    private Scanner.Input input;
    private char c;

    public static final int ident   = 0,
                            number  = 1,
                            lparen  = 2,
                            rparen  = 3,
                            plus    = 4,
                            times   = 5,
                            other   = 6;

    public int sym;
    public String id;
    public String num;

    Scanner.Scanner(Scanner.Input in) {
        this.input = in;
    }

    public void getSym() {
        num = id = "";

        //space
        while (c <= ' ') {
            c = input.next();
        }

        // comment
        if (c == '/') {
            sym = other;
            c = input.next();
            if (c == '*') {
                char pre = c;
                while (!(String.valueOf(pre) + String.valueOf(c)).equals("* /")) {
                    pre = c;
                    c = input.next();
                }
            }
        }

        //String.valueOf(c).matches("[a-zA-Z]");

        // ident
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            sym = ident;
            id = "";
            do {
                id += c;
                c = input.next();
            } while ((c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z') ||
                    (c >= '0' && c <= '9'));
        }
        // number
        else if (c >= '0' && c <= '9') {
            sym = number;
            num = "";
            do {
                num += c;
                c = input.next();
            } while ((c >= '0' && c <= '9'));
        }
        // left bracket
        else if (c == '(') {
            sym = lparen;
            c = input.next();
        }
        // right bracket
        else if (c == ')') {
            sym = rparen;
            c = input.next();
        }
        // plus
        else if (c == '+') {
            sym = plus;
            c = input.next();
        }
        // times
        else if (c == '*') {
            sym = times;
            c = input.next();
        }
        // error
        else {
            sym = other;
            c = input.next();
        }
    }
    */
}
