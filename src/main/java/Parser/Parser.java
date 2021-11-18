package Parser;

import Data.Token;
import Data.TokenType;
import Scanner.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;

public class Parser {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(Parser.class.getName());

    private final Scanner scanner;

    private Token actualToken;
    private Token bufferToken;

    private boolean EOF;
    private boolean FAIL;

    public Parser(String filePath) throws FileNotFoundException {
        this.scanner = new Scanner(filePath);
        scanner.getSym();                       //read first Token in buffer
        bufferToken = scanner.getToken();
        FAIL = false;
    }

    //Start parsing
    public void parseFile() {
        checkClass();
    }


    //-------------------------------------------------------------------------------------------------------

    // structs

    /**
     *  Check: class = “class“ ident classbody
     */
    private void checkClass() {
        if (readNextToken()) {
            if (actualToken.getType().name().equals(TokenType.CLASS.name())) {
                checkIdent();
                checkClassbody();
            } else {
                printError(ParserErrors.ERROR_CLASS.message);
            }
        } else {
            printError(ParserErrors.ERROR_EMPTY.message);
        }
    }

    /**
     *  Check: classbody = ”{“ declarations “}”
     */
    private void checkClassbody() {
        checkLCBracket();
        checkDeclaration();
        checkRCBracket();
        printSuccess();
    }

    /**
     *  Check: declarations =   { “final” type ident “=” expression “;” }
     *                          { type ident “;” }
     *                          { method_declaration }
     */
    private void checkDeclaration() {
        while(bufferToken.getType().name().equals(TokenType.FINAL.name())) {
            readNextToken();    //read buffer to actualToken
            checkType();
            checkIdent();
            checkAssign();
            checkExpression();
            checkSemicolon();
            checkDeclaration(); // check repeating declarations
        }
        while(bufferToken.getType().name().equals(TokenType.INT.name())) {
            readNextToken();    //read buffer to actualToken
            checkIdent();
            checkSemicolon();
            checkDeclaration(); // check repeating declarations
        }
        while(!(bufferToken.getType().name().equals(TokenType.RCBRACKET.name()) ||
                bufferToken.getType().name().equals(TokenType.OTHER.name())     ||
                bufferToken.getType().name().equals(TokenType.EOF.name()))      ){
            checkMethodDeclaration();
            checkDeclaration(); // check repeating declarations
        }
    }

    /**
     *  Check: method_declaration = method_head method_body
     */
    private void checkMethodDeclaration() {
        checkMethodHead();
        checkMethodBody();
    }

    /**
     *  Check: method_head = “public” method_type ident formal_parameters
     */
    private void checkMethodHead() {
            checkPublic();
            checkMethodType();
            checkIdent();
            checkFormalParameters();
    }



    /**
     *  Check: formal_parameters = “(“ [fp_section {“,” fp_section}] “)”
     */
    private void checkFormalParameters() {
        checkLBracket();
        if( bufferToken.getType().name().equals(TokenType.INT.name()) ){
            checkFpSection();
            while( bufferToken.getType().name().equals(TokenType.COMMA.name()) ){
                readNextToken(); //read checked tokens from buffer
                checkFpSection();
            }
        }
        checkRBracket();
    }

    /**
     *  Check: fp_section = type ident
     */
    private void checkFpSection() {
        checkType();
        checkIdent();
    }

    /**
     *  Check: method_body = “{“ {local_declaration} statement_sequence“}”
     */
    private void checkMethodBody() {
        checkLCBracket();
        while ( bufferToken.getType().name().equals(TokenType.INT.name()) ){
            checkLocalDeclaration();
        }
        checkStatementSequenz();
        checkRCBracket();
    }

    /**
     *  Check: local_declaration = type ident “;”
     */
    private void checkLocalDeclaration() {
        checkType();
        checkIdent();
        checkSemicolon();
    }

    /**
     *  Check: statement_sequence = statement {statement}.
     */
    private void checkStatementSequenz() {
        checkStatement();
        while ( bufferToken.getType().name().equals(TokenType.IDENT.name())  |
                bufferToken.getType().name().equals(TokenType.IF.name())     |
                bufferToken.getType().name().equals(TokenType.WHILE.name())  |
                bufferToken.getType().name().equals(TokenType.RETURN.name()) ){
            checkStatement();
        }
    }

    /**
     *  Check: statement = assignment | procedure_call | if_statement | while_statement | return_statement.
     */
    private void checkStatement() {
        if ( bufferToken.getType().name().equals(TokenType.IDENT.name())){
            readNextToken();
            if ( bufferToken.getType().name().equals(TokenType.ASSIGN.name())){
                checkAssignment();
            }else if (bufferToken.getType().name().equals(TokenType.LPAREN.name()) ){
                checkProcedureCall();
            }
        }else if (bufferToken.getType().name().equals(TokenType.IF.name())){
            readNextToken();        //read from buffer
            checkIfStatement();
        }else if (bufferToken.getType().name().equals(TokenType.WHILE.name())){
            readNextToken();        //read from buffer
            checkWhileStatement();
        }else if (bufferToken.getType().name().equals(TokenType.RETURN.name())) {
            readNextToken();        //read from buffer
            checkReturnStatement();
        }else printError(ParserErrors.ERROR_STATEMENT.message);
    }

    /**
     *  Check: assignment = ident “=” expression “;”
     */
    private void checkAssignment() {
        // IDENT already checked in checkStatement()
        checkAssign();
        checkExpression();
        checkSemicolon();
    }

    /**
     *  Check: procedure_call = intern_procedure_call “;”
     */
    private void checkProcedureCall() {
        checkInternProcedureCall();
        checkSemicolon();
    }

    /**
     *  Check: intern_procedure_call = ident actual_parameters
     */
    private void checkInternProcedureCall() {
        // IDENT already checked in checkStatement()
        checkActualParameters();
    }

    /**
     *  Check: if_statement = “if” “(“ expression “)” “{“ statement_sequence “}” “else” “{“ statement_sequence “}”
     */
    private void checkIfStatement() {
        // IF already checked in checkStatement()
        checkLBracket();
        checkExpression();
        checkRBracket();
        checkLCBracket();
        checkStatementSequenz();
        checkRCBracket();
        checkElse();
        checkLCBracket();
        checkStatementSequenz();
        checkRCBracket();
    }

    /**
     *  Check: while_statement = “while” “(“ expression “)” “{“ statement_sequence “}”
     */
    private void checkWhileStatement() {
        // WHILE already checked in checkStatement()
        checkLBracket();
        checkExpression();
        checkRBracket();
        checkLCBracket();
        checkStatementSequenz();
        checkRCBracket();
    }

    /**
     *  Check: return_statement = “return” [ simple_expression ] “;”
     */
    private void checkReturnStatement() {
        // RETURN already checked in checkStatement()
        if ( bufferToken.getType().name().equals(TokenType.IDENT.name()) ||
             bufferToken.getType().name().equals(TokenType.NUMBER.name()) ){
            checkSimpleExpression();
        }
        checkSemicolon();
    }

    /**
     *  Check: actual_parameters = “(“ [expression {“,” expression}]“)”
     */
    private void checkActualParameters() {
        checkLBracket();
        if ( bufferToken.getType().name().equals(TokenType.IDENT.name()) ||
             bufferToken.getType().name().equals(TokenType.NUMBER.name()) ){
            checkExpression();
            while (bufferToken.getType().name().equals(TokenType.COMMA.name())) {
                readNextToken(); //read checked tokens from buffer
                checkExpression();
            }
        }
        checkRBracket();
    }

    /**
     *  Check: expression = simple_expression [(“==” | “<” | ”<= ” | “>” | ”>= ”) simple_expression]
     */
    private void checkExpression() {
        checkSimpleExpression();
        if ( bufferToken.getType().name().equals(TokenType.EQUAL.name())   ||
             bufferToken.getType().name().equals(TokenType.SMALLER.name()) ||
             bufferToken.getType().name().equals(TokenType.SM_EQ.name())   ||
             bufferToken.getType().name().equals(TokenType.GREATER.name()) ||
             bufferToken.getType().name().equals(TokenType.GR_EQ.name())   ){
            readNextToken(); //read checked tokens from buffer
            checkSimpleExpression();
        }
    }

    /**
     *  Check: simple_expression = term {(“+” | ”-” ) term}
     */
    private void checkSimpleExpression() {
        checkTerm();
        while ( bufferToken.getType().name().equals(TokenType.PLUS.name()) ||
                bufferToken.getType().name().equals(TokenType.MINUS.name())) {
            readNextToken(); //read checked tokens from buffer
            checkTerm();
        }
    }

    /**
     *  Check: term = factor {(“*” | ”/ “ ) factor}
     */
    private void checkTerm() {
        checkFactor();
        while ( bufferToken.getType().name().equals(TokenType.TIMES.name()) ||
                bufferToken.getType().name().equals(TokenType.SLASH.name()) ){
            readNextToken(); //read checked tokens from buffer
            checkFactor();
        }
    }

    /**
     *  Check: factor = ident | number | “(“ expression”)” | intern_procedure_call
     */
    private void checkFactor() {
        readNextToken();
        if (actualToken.getType().name().equals(TokenType.LPAREN.name())) {
            checkExpression();
        } else if ( actualToken.getType().name().equals(TokenType.IDENT.name()) &&
                    bufferToken.getType().name().equals(TokenType.LPAREN.name()) ){
            checkInternProcedureCall();
        } else if ( !(actualToken.getType().name().equals(TokenType.IDENT.name()) ||
                      actualToken.getType().name().equals(TokenType.NUMBER.name())) ){
            printError(ParserErrors.ERROR_FACTOR.message);
        }
    }


    //-------------------------------------------------------------------------------------------------------

    // Terminals
    private void checkIdent() {
        if (!bufferToken.getType().name().equals(TokenType.IDENT.name())) {
            printError(ParserErrors.ERROR_IDENT.message);
        }else { readNextToken(); }
    }

    private void checkLCBracket() {
        if (!bufferToken.getType().name().equals(TokenType.LCBRACKET.name())) {
            printError(ParserErrors.ERROR_LCBRACKET.message);
        }else { readNextToken(); }
    }

    private void checkRCBracket() {
        if (!bufferToken.getType().name().equals(TokenType.RCBRACKET.name())) {
            printError(ParserErrors.ERROR_RCBRACKET.message);
        }else { readNextToken(); }
    }

    private void checkSemicolon() {
        if (!bufferToken.getType().name().equals(TokenType.SEMI.name())) {
            printError(ParserErrors.ERROR_SEMI.message);
        }else { readNextToken(); }
    }

    private void checkAssign() {
        if (!bufferToken.getType().name().equals(TokenType.ASSIGN.name())) {
            printError(ParserErrors.ERROR_ASSIGN.message);
        }else { readNextToken(); }
    }

    private void checkType() {
        if (!bufferToken.getType().name().equals(TokenType.INT.name())) {
            printError(ParserErrors.ERROR_TYPE.message);
        }else { readNextToken(); }
    }

    private void checkMethodType() {
        if (!(  bufferToken.getType().name().equals(TokenType.INT.name()) ||
                bufferToken.getType().name().equals(TokenType.VOID.name()) ) ) {
            printError(ParserErrors.ERROR_METHOD_TYPE.message);
        }else { readNextToken(); }
    }

    private void checkLBracket() {
        if (!bufferToken.getType().name().equals(TokenType.LPAREN.name())) {
            printError(ParserErrors.ERROR_LPAREN.message);
        }else { readNextToken(); }
    }

    private void checkRBracket() {
        if (!bufferToken.getType().name().equals(TokenType.RPAREN.name())) {
            printError(ParserErrors.ERROR_RPAREN.message);
        }else { readNextToken(); }
    }

    private void checkElse() {
        if (!bufferToken.getType().name().equals(TokenType.ELSE.name())) {
            printError(ParserErrors.ERROR_ELSE.message);
        }else { readNextToken(); }
    }

    private void checkPublic() {
        if (!bufferToken.getType().name().equals(TokenType.PUBLIC.name())) {
            printError(ParserErrors.ERROR_PUBLIC.message);
        }else { readNextToken(); }
    }

    //-------------------------------------------------------------------------------------------------------

    // Inputs
    private boolean readNextToken() {
        if (EOF) return false;
        if (scanner.hasNext()) {
            actualToken = bufferToken;
            scanner.getSym();
            bufferToken = scanner.getToken();
        } else {
            EOF = true;
            actualToken = bufferToken; //last buffer transfer to actual Token
            bufferToken = scanner.getToken();
        }
        return true;
    }

    //-------------------------------------------------------------------------------------------------------

    // Outputs
    private void printError(String error) {
        logger.error(getPrintFileInfo() + error);
        logger.error(getPrintFileInfo() + getPrintTokens() );
        FAIL = true;
        //System.exit(1);
    }

    private void printSuccess() {
        if(!FAIL) logger.info(getPrintFileInfo() + ParserErrors.NO_ERROR.message);
    }

    public String getPrintFileInfo(){
        return scanner.getToken().getPosition().getFilename() + "("
                + scanner.getToken().getPosition().getLine() + ","
                + scanner.getToken().getPosition().getColumn() + "): ";
    }

    public String getPrintTokens(){
        return    "Actual token: "
                + actualToken.getType().name() + " ["
                + actualToken.getValue() + "]"
                + " | Buffer token: "
                + bufferToken.getType().name() + " ["
                + bufferToken.getValue() + "]";
    }
}
