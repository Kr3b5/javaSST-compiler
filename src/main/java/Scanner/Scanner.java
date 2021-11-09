package Scanner;

import static Scanner.Symbols.*;

/**
 * Compiler - Scanner
 * @author Kr3b5
 */
public class Scanner {

    private Input input;
    private char c;

    /**
     * Symbol
     */
    public int sym;
    /**
     * Ident
     */
    public String id;
    /**
     * Number
     */
    public String num;

    /**
     * Instantiates a new Scanner.
     *
     * @param in Input instance
     */
    Scanner(Input in) {
        this.input = in;
    }


    /**
     * Checks next Symbol
     */
    public void getSym() {
        //reset num & id;
        num = id = "";

        // ignore space
        while (c <= ' '){
            c = input.next();
        }

        // ignore comment /* */
        if (c == '/' && input.getBuffer() == '*') {
            c = input.next();  //skip
            int pre = ' ';
            while (!(pre == '*' && c == '/')) {
                pre = c;
                c = input.next();
            }
            c = input.next();
        }

        // ignore comment //
        if (c == '/' && input.getBuffer() == '/') {
            while (!(c == '\n')) {
                c = input.next();
            }
            c = input.next();
        }

        // ignore space after comments
        while (c <= ' '){
            c = input.next();
        }

        // ident
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            sym = IDENT.id;
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
            sym = NUMBER.id;
            num = "";
            do {
                num += c;
                c = input.next();
            } while ((c >= '0' && c <= '9'));
        }
        // (
        else if (c == '(') {
            sym = LPAREN.id;
            c = input.next();
        }
        // )
        else if (c == ')') {
            sym = RPAREN.id;
            c = input.next();
        }
        // +
        else if (c == '+') {
            sym = PLUS.id;
            c = input.next();
        }
        // -
        else if (c == '-') {
            sym = MINUS.id;
            c = input.next();
        }
        // *
        else if (c == '*') {
            sym = TIMES.id;
            c = input.next();
        }
        // /
        else if (c == '/') {
            sym = SLASH.id;
            c = input.next();
        }
        // [
        else if (c == '[') {
            sym = RBRACKET.id;
            c = input.next();
        }
        // ]
        else if (c == ']') {
            sym = LBRACKET.id;
            c = input.next();
        }
        // ,
        else if (c == ',') {
            sym = COMMA.id;
            c = input.next();
        }
        // ;
        else if (c == ';') {
            sym = SEMI.id;
            c = input.next();
        }
        // =
        else if (c == '=') {
            sym = ASSIGN.id;
            // ==
            if (input.getBuffer() == '=') {
                sym = EQUAL.id;
                c = input.next();
            }
            c = input.next();
        }
        // >
        else if (c == '>') {
            sym = GREATER.id;
            // >=
            if (input.getBuffer() == '=') {
                sym = GR_EQ.id;
                c = input.next();
            }
            c = input.next();
        }
        // <
        else if (c == '<') {
            sym = SMALLER.id;
            // <=
            if (input.getBuffer() == '=') {
                sym = SM_EQ.id;
                c = input.next();
            }
            c = input.next();
        }
        // !=
        else if (c == '!' && input.getBuffer() == '=') {
            sym = NEQUAL.id;
            c = input.next();
            c = input.next();
        }
        // {
        else if (c == '{') {
            sym = LCBRACKET.id;
            c = input.next();
        }
        // }
        else if (c == '}') {
            sym = RCBRACKET.id;
            c = input.next();
        }
        // OTHER
        else{
            sym = OTHER.id;
            c = input.next();
        }


    }
}
