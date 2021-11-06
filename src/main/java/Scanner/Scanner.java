package Scanner;

import static Scanner.Symbols.*;

public class Scanner {

    private Input input;
    private char c;

    public int sym;
    public String id;
    public String num;

    Scanner(Input in) {
        this.input = in;
    }


    public void getSym() {
        //reset num & id;
        num = id = "";

        // ignore space
        if (c <= ' ') c = input.next();

        // ignore comment
        if (c == '/' && input.getBuffer() == '*') {
            c = input.next();  //skip
            int pre = ' ';
            while (!(pre == '*' && c == '/')) {
                pre = c;
                c = input.next();
            }
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
        }else{
            sym = OTHER.id;
            c = input.next();
        }

    }
}
