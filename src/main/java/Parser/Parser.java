package Parser;

import Data.Token;
import Scanner.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;

public class Parser {

    /** The logger for this class. */
    private static final Logger logger = LogManager.getLogger(Parser.class.getName());

    private final Scanner scanner;

    private Token actualToken;

    public Parser(String filePath) throws FileNotFoundException {
        this.scanner = new Scanner(filePath);
    }


    private boolean readNextToken(){
        if (scanner.hasNext()){
            scanner.getSym();
            actualToken =  scanner.getToken();
            //printTest();
            return true;
        }else{
            return false;
        }
    }


    public void parseFile() {
        checkClass();
    }


    private void checkClass(){
        if(readNextToken()){
            if( actualToken.getType().name().equals("CLASS")){
                checkIdent();
                checkClassbody();
            }else{
                printError(ParserErrors.ERROR_CLASS.message);
            }
        }
    }


    private void checkIdent() {
        if(readNextToken()){
            if(!actualToken.getType().name().equals("IDENT")){
                printError(ParserErrors.ERROR_IDENT.message);
            }
        }else{
            printError(ParserErrors.ERROR_EOF.message);
        }
    }


    private void checkClassbody() {
        checkLCBracket();
        //TODO: add Class
        //checkDeclaration();
        checkRCBracket();
    }

    private void checkLCBracket(){
        if(readNextToken()){
            if(!actualToken.getType().name().equals("LCBRACKET")){
                printError(ParserErrors.ERROR_LCBRACKET.message);
            }
        }else{
            printError(ParserErrors.ERROR_EOF.message);
        }
    }

    private void checkRCBracket(){
        if(readNextToken()){
            if(!actualToken.getType().name().equals("RCBRACKET")){
                printError(ParserErrors.ERROR_RCBRACKET.message);
            }
        }else{
            printError(ParserErrors.ERROR_EOF.message);
        }
    }

    private void printError(String error){
        System.out.println( scanner.getToken().getPosition().getFilename()    + "("
                            + scanner.getToken().getPosition().getLine()      + ","
                            + scanner.getToken().getPosition().getColumn()    + "): "
                            + error);

        System.exit(1);
    }

    private void printTest(){
        System.out.println( scanner.getToken().getPosition().getFilename()    + "("
                + scanner.getToken().getPosition().getLine()      + ","
                + scanner.getToken().getPosition().getColumn()    + "): "
                + scanner.getToken().getType().name());
    }
}
