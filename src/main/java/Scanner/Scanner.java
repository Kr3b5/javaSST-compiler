package Scanner;

import static Scanner.Symbols.*;

/**
 * Compiler - Scanner
 * @author Kr3b5
 */
public class Scanner {

    private final Input input;
    private char c;
    private int position = 0;

    public Token token;

    public String id;
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

        skipSpace();
        skipComment();

        // ident
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            token = new Token(IDENT, position);
            id = "";
            do {
                id += c;
                c = input.next();
            } while ((c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z') ||
                    (c >= '0' && c <= '9'));
            checkkeyword();
        }
        // number
        else if (c >= '0' && c <= '9') {
            token = new Token(NUMBER, position);
            num = "";
            do {
                num += c;
                c = input.next();
            } while ((c >= '0' && c <= '9'));
        }
        // (
        else if (c == '(') {
            token = new Token(LPAREN, position);
            c = input.next();
        }
        // )
        else if (c == ')') {
            token = new Token(RPAREN, position);
            c = input.next();
        }
        // +
        else if (c == '+') {
            token = new Token(PLUS, position);
            c = input.next();
        }
        // -
        else if (c == '-') {
            token = new Token(MINUS, position);
            c = input.next();
        }
        // *
        else if (c == '*') {
            token = new Token(TIMES, position);
            c = input.next();
        }
        // /
        else if (c == '/') {
            token = new Token(SLASH, position);
            c = input.next();
        }
        // ,
        else if (c == ',') {
            token = new Token(COMMA, position);
            c = input.next();
        }
        // ;
        else if (c == ';') {
            token = new Token(SEMI, position);
            c = input.next();
        }
        // =
        else if (c == '=') {
            token = new Token(ASSIGN, position);
            // ==
            if (input.getBuffer() == '=') {
                token = new Token(EQUAL, position);
                c = input.next();
            }
            c = input.next();
        }
        // >
        else if (c == '>') {
            token = new Token(GREATER, position);
            // >=
            if (input.getBuffer() == '=') {
                token = new Token(GR_EQ, position);
                c = input.next();
            }
            c = input.next();
        }
        // <
        else if (c == '<') {
            token = new Token(SMALLER, position);
            // <=
            if (input.getBuffer() == '=') {
                token = new Token(SM_EQ, position);
                c = input.next();
            }
            c = input.next();
        }
        // !=
        else if (c == '!' && input.getBuffer() == '=') {
            token = new Token(NEQUAL, position);
            c = input.next();
            c = input.next();
        }
        // {
        else if (c == '{') {
            token = new Token(LCBRACKET, position);
            c = input.next();
        }
        // }
        else if (c == '}') {
            token = new Token(RCBRACKET, position);
            c = input.next();
        }
        // OTHER
        else{
            token = new Token(OTHER, position);
            c = input.next();
        }

        position++;
    }

    private void checkkeyword(){
        switch (id) {
            case "class"    -> token = new Token(CLASS, position);
            case "public"   -> token = new Token(PUBLIC, position);
            case "final"    -> token = new Token(FINAL, position);
            case "void"     -> token = new Token(VOID, position);
            case "int"      -> token = new Token(INT, position);
            case "if"       -> token = new Token(IF, position);
            case "else"     -> token = new Token(ELSE, position);
            case "while"    -> token = new Token(WHILE, position);
            case "return"   -> token = new Token(RETURN, position);
            default -> {}
        }
    }


    private void skipSpace(){
        // ignore space
        while (c <= ' '){
            c = input.next();
        }
    }

    private void skipComment(){
        // ignore comment /* */ and /** */
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

        skipSpace();

        if ((c == '/' && input.getBuffer() == '*')||(c == '/' && input.getBuffer() == '/')) {
            skipComment();
        }
    }
}
