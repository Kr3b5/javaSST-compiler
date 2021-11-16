package Scanner;

import Data.Token;
import Data.TokenType;

import java.io.File;
import java.io.FileNotFoundException;

import static Data.TokenType.*;

/**
 * Compiler - Scanner
 * @author Kr3b5
 */
public class Scanner {

    private final Input input;
    private char c;

    private String file;
    private int line;
    private int column;

    private Token token;

    private String value;

    /**
     * Instantiates a new Scanner.
     *
     * @param filepath Input instance
     */
    Scanner(String filepath) throws FileNotFoundException {
        this.input = new Input(filepath);
        file = new File(filepath).getName();
        line = 1;
        column = 0;
    }

    public Token getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }


    /**
     * Checks next Symbol
     */
    public void getSym() {
        //reset value
        value = "";

        skipSpace();
        skipComment();

        // ident
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            int startColumn = column;
            do {
                value += c;
                loadNext();
            } while ((c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z') ||
                    (c >= '0' && c <= '9'));
            isKeywordorIdent(startColumn);
        }
        // number
        else if (c >= '0' && c <= '9') {
            int startColumn = column;
            do {
                value += c;
                loadNext();
            } while ((c >= '0' && c <= '9'));
            token = new Token(NUMBER, file, line, startColumn, value);
        }
        // (
        else if (c == '(') {
            token = new Token(LPAREN, file, line, column);
            loadNext();
        }
        // )
        else if (c == ')') {
            token = new Token(RPAREN, file, line, column);
            loadNext();
        }
        // +
        else if (c == '+') {
            token = new Token(PLUS, file, line, column);
            loadNext();
        }
        // -
        else if (c == '-') {
            token = new Token(MINUS, file, line, column);
            loadNext();
        }
        // *
        else if (c == '*') {
            token = new Token(TIMES, file, line, column);
            loadNext();
        }
        // /
        else if (c == '/') {
            token = new Token(SLASH, file, line, column);
            loadNext();
        }
        // ,
        else if (c == ',') {
            token = new Token(COMMA, file, line, column);
            loadNext();
        }
        // ;
        else if (c == ';') {
            token = new Token(SEMI, file, line, column);
            loadNext();
        }
        // =
        else if (c == '=') {
            token = new Token(ASSIGN, file, line, column);
            // ==
            if (input.getBuffer() == '=') {
                token = new Token(EQUAL, file, line, column - 1);
                loadNext();
            }
            loadNext();
        }
        // >
        else if (c == '>') {
            token = new Token(GREATER, file, line, column);
            // >=
            if (input.getBuffer() == '=') {
                token = new Token(GR_EQ, file, line, column - 1);
                loadNext();
            }
            loadNext();
        }
        // <
        else if (c == '<') {
            token = new Token(SMALLER, file, line, column);
            // <=
            if (input.getBuffer() == '=') {
                token = new Token(SM_EQ, file, line, column - 1);
                loadNext();
            }
            loadNext();
        }
        // !=
        else if (c == '!' && input.getBuffer() == '=') {
            token = new Token(NEQUAL, file, line, column);
            loadNext();
            loadNext();
        }
        // {
        else if (c == '{') {
            token = new Token(LCBRACKET, file, line, column);
            loadNext();
        }
        // }
        else if (c == '}') {
            token = new Token(RCBRACKET, file, line, column);
            loadNext();
        }
        // OTHER
        else{
            token = new Token(OTHER, file, line, column);
            loadNext();
        }
    }

    private void isKeywordorIdent(int column){
        switch (value) {
            case "class"    -> token = new Token(CLASS, file, line, column);
            case "public"   -> token = new Token(PUBLIC, file, line, column);
            case "final"    -> token = new Token(FINAL, file, line, column);
            case "void"     -> token = new Token(VOID, file, line, column);
            case "int"      -> token = new Token(INT, file, line, column);
            case "if"       -> token = new Token(IF, file, line, column);
            case "else"     -> token = new Token(ELSE, file, line, column);
            case "while"    -> token = new Token(WHILE, file, line, column);
            case "return"   -> token = new Token(RETURN, file, line, column);
            default         -> token = new Token(IDENT, file, line, column, value);
        }
    }


    private void skipSpace(){
        // ignore space
        while (c <= ' '){
            loadNext();
        }
    }

    private void skipComment(){
        // ignore comment /* */ and /** */
        if (c == '/' && input.getBuffer() == '*') {
            loadNext();  //skip
            int pre = ' ';
            while (!(pre == '*' && c == '/')) {
                pre = c;
                loadNext();
            }
            loadNext();
        }

        // ignore comment //
        if (c == '/' && input.getBuffer() == '/') {
            while (!(c == '\n')) {
                loadNext();
            }
            loadNext();
        }

        skipSpace();

        if ((c == '/' && input.getBuffer() == '*')||(c == '/' && input.getBuffer() == '/')) {
            skipComment();
        }
    }

    private void loadNext(){
        c = input.next();
        column++;
        if (c == '\n') setNewline();
    }

    private void setNewline() {
        line++;
        column = 0;
    }

    public boolean hasNext(){
        if(input.hasNext()){
            return true;
        }else{
            column++;
            token = new Token(TokenType.EOF, file, line, column);
            return false;
        }
    }

}
