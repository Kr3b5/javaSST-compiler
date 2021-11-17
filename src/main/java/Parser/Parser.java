package Parser;

import Data.Token;
import Data.TokenType;
import Scanner.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;

public class Parser {

    /** The logger for this class. */
    private static final Logger logger = LogManager.getLogger(Parser.class.getName());

    private final Scanner scanner;

    private Token actualToken;
    private Token bufferToken;

    private boolean EOF;

    public Parser(String filePath) throws FileNotFoundException {
        this.scanner = new Scanner(filePath);
        scanner.getSym();                       //read first Token in buffer
        bufferToken = scanner.getToken();
    }

    //Start parsing
    public void parseFile() {
        checkClass();
    }

    //TODO: OPTIONAL []


    //-------------------------------------------------------------------------------------------------------

    // structs
    private void checkClass(){
        if(readNextToken()){
            if( actualToken.getType().name().equals(TokenType.CLASS.name())){
                checkIdent();
                checkClassbody();
            }else{
                printError(ParserErrors.ERROR_CLASS.message);
            }
        }
    }

    private void checkClassbody() {
        checkLCBracket();
        checkDeclaration();
        checkRCBracket_noRead();
    }

    private void checkDeclaration() {
        if(readNextToken()){
            // TODO wiederholen
            if(actualToken.getType().name().equals(TokenType.FINAL.name())){
                checkType();
                checkIdent();
                checkAssign();
                checkExpression(true);
                checkSemicolon();
            }else if(actualToken.getType().name().equals(TokenType.INT.name())){
                checkIdent();
                checkSemicolon();
            }else if(!actualToken.getType().name().equals(TokenType.RCBRACKET.name())){
                checkMethodDeclaration();
            }
        }
    }

    private void checkMethodDeclaration() {
        checkMethodHead();
        checkMethodBody();
    }

    private void checkMethodHead() {
        if(actualToken.getType().name().equals(TokenType.PUBLIC.name())){
            checkMethodType();
            checkIdent();
            checkFormalParameters();
        }else{
            printError(ParserErrors.ERROR_PUBLIC.message);
        }

    }

    private void checkFormalParameters() {
        checkLBracket();
        do{
            checkFpSection();
        }while (readNextToken() && actualToken.getType().name().equals(TokenType.COMMA.name()));
        checkRBracket();
    }

    private void checkFpSection() {
        checkType();
        checkIdent();
    }


    private void checkMethodBody() {
        checkLCBracket();
        //TODO nothing or sth
        checkLocalDeclaration();
        checkStatementSequenz();
        checkRCBracket();
    }

    private void checkLocalDeclaration() {
        checkType();
        checkIdent();
        checkSemicolon();
    }


    private void checkStatementSequenz() {
        checkStatement(true);
        while (readNextToken() && (
                        actualToken.getType().name().equals(TokenType.IDENT.name()) |
                        actualToken.getType().name().equals(TokenType.IF.name())    |
                        actualToken.getType().name().equals(TokenType.WHILE.name()) |
                        actualToken.getType().name().equals(TokenType.RETURN.name()) ) ){
            checkStatement(false);
        }
    }


    private void checkStatement(boolean readin) {
        if(readin) readNextToken();
        if(actualToken.getType().name().equals(TokenType.IDENT.name())){
            if(readNextToken() && actualToken.getType().name().equals(TokenType.ASSIGN.name())){
                checkAssignment();
            }else if (readNextToken() && actualToken.getType().name().equals(TokenType.LPAREN.name())){
                checkProcedureCall();
            }else{
                printError(ParserErrors.ERROR_STATEMENT.message);
            }
        }
        if(actualToken.getType().name().equals(TokenType.IF.name()))            checkIfStatement();
        else if(actualToken.getType().name().equals(TokenType.WHILE.name()))    checkWhileStatement();
        else if(actualToken.getType().name().equals(TokenType.RETURN.name()))   checkReturnStatement();
        else printError(ParserErrors.ERROR_STATEMENT.message);
    }

    private void checkAssignment() {
        // IDENT + "=" already checked in checkStatement()
        checkExpression(true);
        checkSemicolon();
    }

    private void checkProcedureCall() {
        checkInternProcedureCall();
        checkSemicolon();
    }

    private void checkInternProcedureCall() {
        // IDENT already checked in checkStatement()
        checkActualParameters();
    }

    private void checkIfStatement() {
        // IF already checked in checkStatement()
        checkLBracket();
        checkExpression(true);
        checkRBracket();
        checkLCBracket();
        checkStatementSequenz();
        checkRCBracket();
        checkElse();
        checkLCBracket();
        checkStatementSequenz();
        checkRCBracket();
    }

    private void checkWhileStatement() {
        // WHILE already checked in checkStatement()
        checkRBracket();
        checkExpression(true);
        checkLBracket();
        checkRCBracket();
        checkStatementSequenz();
        checkRCBracket();
    }

    private void checkReturnStatement() {
        // RETURN already checked in checkStatement()
        if(readNextToken() && ( actualToken.getType().name().equals(TokenType.IDENT.name()) ||
                                actualToken.getType().name().equals(TokenType.NUMBER.name())) ){
            checkSimpleExpression(false);
        }else if(!actualToken.getType().name().equals(TokenType.SEMI.name())){
            printError(ParserErrors.ERROR_SEMI.message);
        }
    }

    private void checkActualParameters() {
        // LPAREN already checked in checkStatement()
        if(readNextToken() &&  (actualToken.getType().name().equals(TokenType.IDENT.name())||
                                actualToken.getType().name().equals(TokenType.NUMBER.name()) )){
            checkExpression(false);
            while (readNextToken() && actualToken.getType().name().equals(TokenType.COMMA.name())){
                checkExpression(true);
            }
        }else if(!actualToken.getType().name().equals(TokenType.RPAREN.name())){
            printError(ParserErrors.ERROR_RPAREN.message);
        }
    }

    private void checkExpression(boolean readin) {
        if(readin) readNextToken();
        checkSimpleExpression(false);
        if( readNextToken() &&  ( actualToken.getType().name().equals(TokenType.EQUAL.name())     ||
                                  actualToken.getType().name().equals(TokenType.SMALLER.name())   ||
                                  actualToken.getType().name().equals(TokenType.SM_EQ.name())     ||
                                  actualToken.getType().name().equals(TokenType.GREATER.name())   ||
                                  actualToken.getType().name().equals(TokenType.GR_EQ.name()) )){
            checkSimpleExpression(true);
        }else{
            printError(ParserErrors.ERROR_SIMPLE_EXP.message);
        }
    }

    private void checkSimpleExpression(boolean readin) {
        if(readin) readNextToken();
        checkTerm();
        while ( bufferToken.getType().name().equals(TokenType.PLUS.name())  ||
                bufferToken.getType().name().equals(TokenType.MINUS.name()) ){
            readNextToken();
            checkTerm();
        }
    }

    private void checkTerm() {
        checkFactor();
        while ( bufferToken.getType().name().equals(TokenType.TIMES.name())  ||
                bufferToken.getType().name().equals(TokenType.SLASH.name()) ){
            readNextToken();
            checkFactor();
        }
    }

    private void checkFactor() {
        if( actualToken.getType().name().equals(TokenType.LPAREN.name())  ){
            checkExpression(true);
        }else if(  actualToken.getType().name().equals(TokenType.IDENT.name()) &&
                   bufferToken.getType().name().equals(TokenType.LPAREN.name()) ){
            checkInternProcedureCall();
        }else if(!actualToken.getType().name().equals(TokenType.IDENT.name()) ||
                 !actualToken.getType().name().equals(TokenType.NUMBER.name())){
            printError(ParserErrors.ERROR_FACTOR.message);
        }
    }


    //-------------------------------------------------------------------------------------------------------

    // Terminals without readNextToken
    private void checkRCBracket_noRead(){
        if( !actualToken.getType().name().equals(TokenType.RCBRACKET.name()) ){
            printError(ParserErrors.ERROR_RCBRACKET.message);
        }
    }


    // Terminals
    private void checkIdent() {
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.IDENT.name())){
            printError(ParserErrors.ERROR_IDENT.message);
        }
    }

    private void checkLCBracket(){
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.LCBRACKET.name())){
            printError(ParserErrors.ERROR_LCBRACKET.message);
        }
    }

    private void checkRCBracket() {
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.RCBRACKET.name())){
            printError(ParserErrors.ERROR_RCBRACKET.message);
        }
    }

    private void checkSemicolon() {
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.SEMI.name()) ){
            printError(ParserErrors.ERROR_SEMI.message);
        }
    }

    private void checkAssign() {
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.ASSIGN.name()) ){
            printError(ParserErrors.ERROR_ASSIGN.message);
        }
    }

    private void checkType() {
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.INT.name()) ){
            printError(ParserErrors.ERROR_INT.message);
        }
    }

    private void checkMethodType() {
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.INT.name()) |
                                !actualToken.getType().name().equals(TokenType.VOID.name()) ){
            printError(ParserErrors.ERROR_METHOD_TYPE.message);
        }
    }

    private void checkLBracket() {
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.LPAREN.name()) ){
            printError(ParserErrors.ERROR_LPAREN.message);
        }
    }

    private void checkRBracket() {
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.RPAREN.name()) ){
            printError(ParserErrors.ERROR_RPAREN.message);
        }
    }

    private void checkElse() {
        if(!readNextToken() || !actualToken.getType().name().equals(TokenType.ELSE.name()) ){
            printError(ParserErrors.ERROR_ELSE.message);
        }
    }

    //-------------------------------------------------------------------------------------------------------

    // Inputs
    private boolean readNextToken(){
       if (EOF) return false;
       if (scanner.hasNext()){
            actualToken =  bufferToken;
            scanner.getSym();
            bufferToken = scanner.getToken();
        }else{
           EOF = true;
           actualToken =  bufferToken; //last buffer transfer to actual Token
        }
        return true;
    }

    //-------------------------------------------------------------------------------------------------------

    // Outputs
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
