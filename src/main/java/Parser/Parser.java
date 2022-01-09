package Parser;

import AST.*;
import Data.*;
import Scanner.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

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

    private SymbolTable symbolTable;
    private SymbolTable symbolTableBuffer;
    private String ST_ID;
    private int ST_VALUE;
    private String ST_M_Type;

    private AST ast;
    private int astID;
    private ASTNode bufferNode;

    private final List<ASTNode> finalNodes;
    private final List<ASTNode> varNodes;
    private final List<ASTNode> methodNodes;
    private ASTNodeContainer nodeContainerFinal;
    private ASTNodeContainer nodeContainerVars;
    private ASTNodeContainer nodeContainerMethods;

    private String AST_ID;


    public Parser(String filePath) throws FileNotFoundException {
        this.scanner = new Scanner(filePath);
        scanner.getSym();                       //read first Token in buffer
        bufferToken = scanner.getToken();
        FAIL = false;
        this.symbolTable = new SymbolTable();
        ST_ID = null;
        astID = 1;

        finalNodes = new LinkedList<ASTNode>();
        varNodes = new LinkedList<ASTNode>();
        methodNodes = new LinkedList<ASTNode>();
    }

    //Start parsing
    public void parseFile() {
        checkClass();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public AST getAst() { return ast; }

    //-------------------------------------------------------------------------------------------------------

    // structs

    /**
     *  Check: class = “class“ ident classbody
     */
    private void checkClass() {
        if (readNextToken()) {
            if (actualToken.getType().name().equals(TokenType.CLASS.name())) {
                checkIdent();

                SymbolTable newST = new SymbolTable(symbolTable);
                STObject stClassObj = new STObject(ST_ID, ObjClass.CLASS,newST);
                SymbolTableInsert(stClassObj);
                SymbolTable prev = symbolTable;
                symbolTable = newST;

                // AST
                ast = new AST(astID++, stClassObj);
                createContainer();

                checkClassbody();

                mergeContainers();

                symbolTable = prev;
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

            STObject stFinalObj = new STObject(ST_ID, ObjClass.CONST, STType.INT,ST_VALUE);
            ASTNode consNode = new ASTNode(astID++,ST_VALUE);

            SymbolTableInsert(stFinalObj);

            finalNodes.add(new ASTNode(astID++, consNode, stFinalObj));

            checkDeclaration(); // check repeating declarations
        }
        while(bufferToken.getType().name().equals(TokenType.INT.name())) {
            readNextToken();    //read buffer to actualToken
            checkIdent();
            checkSemicolon();

            STObject stVarObj = new STObject(ST_ID, ObjClass.VAR, STType.INT);
            SymbolTableInsert(stVarObj);

            varNodes.add(new ASTNode(astID++, stVarObj));

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

        symbolTable = symbolTableBuffer;
    }

    /**
     *  Check: method_head = “public” method_type ident formal_parameters
     */
    private void checkMethodHead() {
            checkPublic();
            checkMethodType();
            checkIdent();

            SymbolTable newST = new SymbolTable(symbolTable);
            STObject stMethodObj;
            if(ST_M_Type.equals("VOID")){
                stMethodObj = new STObject(ST_ID, ObjClass.PROC, STType.VOID, newST);
                SymbolTableInsert(stMethodObj);
            }else{
                stMethodObj = new STObject(ST_ID, ObjClass.PROC, STType.INT, newST);
                SymbolTableInsert(stMethodObj);
            }
            symbolTableBuffer = symbolTable;
            symbolTable = newST;

            bufferNode = new ASTNode(astID++, stMethodObj);
            methodNodes.add(bufferNode);

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

        SymbolTableInsert(new STObject(ST_ID, ObjClass.PAR, STType.INT));
    }

    /**
     *  Check: method_body = “{“ {local_declaration} statement_sequence“}”
     */
    private void checkMethodBody() {
        checkLCBracket();
        while ( bufferToken.getType().name().equals(TokenType.INT.name()) ){
            checkLocalDeclaration();
        }
        ASTNode mbNode = checkStatementSequenz();
        checkRCBracket();

        bufferNode.setLink(mbNode);
    }

    /**
     *  Check: local_declaration = type ident “;”
     */
    private void checkLocalDeclaration() {
        checkType();
        checkIdent();
        checkSemicolon();

        SymbolTableInsert(new STObject(ST_ID, ObjClass.VAR, STType.INT));
    }

    /**
     *  Check: statement_sequence = statement {statement}.
     */
    private ASTNode checkStatementSequenz() {
        ASTNode last, actual, first;
        last = checkStatement();
        first = last;
        while ( bufferToken.getType().name().equals(TokenType.IDENT.name())  |
                bufferToken.getType().name().equals(TokenType.IF.name())     |
                bufferToken.getType().name().equals(TokenType.WHILE.name())  |
                bufferToken.getType().name().equals(TokenType.RETURN.name()) ){
            actual = checkStatement();

            last.setLink(actual);
            last = actual;
        }
        return first;
    }

    /**
     *  Check: statement = assignment | procedure_call | if_statement | while_statement | return_statement.
     */
    private ASTNode checkStatement() {
        ASTNode statementNode = null; ;
        if ( bufferToken.getType().name().equals(TokenType.IDENT.name())){
            AST_ID = bufferToken.getValue();
            readNextToken();
            if ( bufferToken.getType().name().equals(TokenType.ASSIGN.name())){
                statementNode = checkAssignment();
            }else if (bufferToken.getType().name().equals(TokenType.LPAREN.name()) ){
                checkProcedureCall();
            }
        }else if (bufferToken.getType().name().equals(TokenType.IF.name())){
            readNextToken();        //read from buffer
            statementNode = checkIfStatement();
        }else if (bufferToken.getType().name().equals(TokenType.WHILE.name())){
            readNextToken();        //read from buffer
            statementNode = checkWhileStatement();
        }else if (bufferToken.getType().name().equals(TokenType.RETURN.name())) {
            readNextToken();        //read from buffer
            statementNode = checkReturnStatement();
        }else printError(ParserErrors.ERROR_STATEMENT.message);

        return statementNode;
    }

    /**
     *  Check: assignment = ident “=” expression “;”
     */
    private ASTNode checkAssignment() {
        // IDENT already checked in checkStatement()
        ASTNode identNode = new ASTNode(astID++, AST_ID, ASTClass.VAR);

        checkAssign();
        ASTNode expressionNode = checkExpression();
        checkSemicolon();

        return new ASTNode(astID++, ASTClass.ASSIGN, identNode, expressionNode);
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
    private ASTNode checkInternProcedureCall() {
        // IDENT already checked in checkStatement()
        ASTNode internProcedureNode = new ASTNode(astID++, actualToken.getValue(), ASTClass.PROD);
        checkActualParameters();

        return internProcedureNode;
    }

    /**
     *  Check: if_statement = “if” “(“ expression “)” “{“ statement_sequence “}” “else” “{“ statement_sequence “}”
     */
    private ASTNode checkIfStatement() {
        // IF already checked in checkStatement()
        ASTNode ifelseNode = new ASTNode(astID++, ASTClass.IF_ELSE);
        ASTNode ifNode = new ASTNode(astID++, ASTClass.IF);
        ASTNode statementSeqNode;

        checkLBracket();
        ASTNode expressionNode = checkExpression();
        checkRBracket();

        ifNode.setLeft(expressionNode);

        checkLCBracket();
        statementSeqNode = checkStatementSequenz();
        checkRCBracket();

        ifNode.setRight(statementSeqNode);
        ifelseNode.setLeft(ifNode);

        checkElse();
        checkLCBracket();
        statementSeqNode = checkStatementSequenz();
        checkRCBracket();

        ifelseNode.setRight(statementSeqNode);

        return ifelseNode;
    }

    /**
     *  Check: while_statement = “while” “(“ expression “)” “{“ statement_sequence “}”
     */
    private ASTNode checkWhileStatement() {
        // WHILE already checked in checkStatement()
        ASTNode whileNode = new ASTNode(astID++, ASTClass.WHILE);

        checkLBracket();
        ASTNode expressionNode = checkExpression();
        checkRBracket();

        whileNode.setLeft(expressionNode);

        checkLCBracket();
        ASTNode statementSeqNode = checkStatementSequenz();
        checkRCBracket();

        whileNode.setRight(statementSeqNode);

        return whileNode;
    }

    /**
     *  Check: return_statement = “return” [ simple_expression ] “;”
     */
    private ASTNode checkReturnStatement() {
        // RETURN already checked in checkStatement()
        ASTNode simpleExpressionNode = null;
        if ( bufferToken.getType().name().equals(TokenType.IDENT.name()) ||
             bufferToken.getType().name().equals(TokenType.NUMBER.name()) ){
            simpleExpressionNode = checkSimpleExpression();
        }
        checkSemicolon();

        ASTNode returnNode = new ASTNode(astID++, ASTClass.RETURN, simpleExpressionNode);
        return returnNode;
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
    private ASTNode checkExpression() {
        ASTNode simpleExpressionNode = checkSimpleExpression();
        ASTNode ExpressionNode = simpleExpressionNode;
        if ( bufferToken.getType().name().equals(TokenType.EQUAL.name())   ||
             bufferToken.getType().name().equals(TokenType.SMALLER.name()) ||
             bufferToken.getType().name().equals(TokenType.SM_EQ.name())   ||
             bufferToken.getType().name().equals(TokenType.GREATER.name()) ||
             bufferToken.getType().name().equals(TokenType.GR_EQ.name())   ){

            ExpressionNode = new ASTNode(astID++, simpleExpressionNode, ASTClass.BINOP, bufferToken.getType());

            readNextToken(); //read checked tokens from buffer
            simpleExpressionNode = checkSimpleExpression();

            ExpressionNode.setRight(simpleExpressionNode);
        }
        return ExpressionNode;
    }

    /**
     *  Check: simple_expression = term {(“+” | ”-” ) term}
     */
    private ASTNode checkSimpleExpression() {
        ASTNode termNode = checkTerm();
        ASTNode simpleExpressionNode = termNode;
        while ( bufferToken.getType().name().equals(TokenType.PLUS.name()) ||
                bufferToken.getType().name().equals(TokenType.MINUS.name())) {

            simpleExpressionNode = new ASTNode(astID++, termNode, ASTClass.BINOP, bufferToken.getType());

            readNextToken(); //read checked tokens from buffer
            termNode = checkTerm();

            simpleExpressionNode.setRight(termNode);
        }
        return simpleExpressionNode;
    }

    /**
     *  Check: term = factor {(“*” | ”/ “ ) factor}
     */
    private ASTNode checkTerm() {
        ASTNode factorNode = checkFactor();
        ASTNode termNode = factorNode;
        while ( bufferToken.getType().name().equals(TokenType.TIMES.name()) ||
                bufferToken.getType().name().equals(TokenType.SLASH.name()) ){

            termNode  =  new ASTNode(astID++, factorNode, ASTClass.BINOP, bufferToken.getType());

            readNextToken(); //read checked tokens from buffer
            factorNode = checkFactor();

            termNode.setRight(factorNode);
        }
        return termNode;
    }

    /**
     *  Check: factor = ident | number | “(“ expression”)” | intern_procedure_call
     */
    private ASTNode checkFactor() {
        ASTNode factorNode = null;
        readNextToken();
        if (actualToken.getType().name().equals(TokenType.LPAREN.name())) {
            factorNode = checkExpression();
            checkRBracket();
        } else if ( actualToken.getType().name().equals(TokenType.IDENT.name()) &&
                    bufferToken.getType().name().equals(TokenType.LPAREN.name()) ){
            factorNode = checkInternProcedureCall();
        } else if ( !(actualToken.getType().name().equals(TokenType.IDENT.name()) ||
                      actualToken.getType().name().equals(TokenType.NUMBER.name())) ){
            printError(ParserErrors.ERROR_FACTOR.message);
        }

        if(actualToken.getType().name().equals(TokenType.NUMBER.name())){
            ST_VALUE = Integer.parseInt(actualToken.getValue());
            factorNode = new ASTNode(astID++, ST_VALUE);
        }
        if(actualToken.getType().name().equals(TokenType.IDENT.name())){
            factorNode = new ASTNode(astID++, actualToken.getValue(), ASTClass.VAR);
        }
        return factorNode;
    }


    //-------------------------------------------------------------------------------------------------------

    // Terminals
    private void checkIdent() {
        if (!bufferToken.getType().name().equals(TokenType.IDENT.name())) {
            printError(ParserErrors.ERROR_IDENT.message);
        }else {
            ST_ID = bufferToken.getValue();
            readNextToken();
        }
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
        }else {
            if(bufferToken.getType().name().equals(TokenType.INT.name())){
                ST_M_Type = "INT";
            }else{ ST_M_Type = "VOID"; }
            readNextToken();
        }
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
        System.exit(1);
    }

    private void printSuccess() {
        if(!FAIL) logger.info(getPrintFileInfo() + ParserErrors.NO_ERROR.message);
    }

    private String getPrintFileInfo(){
        return scanner.getToken().getPosition().getFilename() + "("
                + scanner.getToken().getPosition().getLine() + ","
                + scanner.getToken().getPosition().getColumn() + "): ";
    }

    private String getPrintTokens(){
        return    "Actual token: "
                + actualToken.getType().name() + " ["
                + actualToken.getValue() + "]"
                + " | Buffer token: "
                + bufferToken.getType().name() + " ["
                + bufferToken.getValue() + "]";
    }

    //-------------------------------------------------------------------------------------------------------

    // SymbolTable Helper

    private void SymbolTableInsert(STObject obj){
        symbolTable.insert(obj);
        resetValues();
    }

    private void resetValues(){
        ST_VALUE = 0;
        ST_ID = null;
    }

    //-------------------------------------------------------------------------------------------------------
    // AST Helper

    private void createContainer(){
        nodeContainerFinal = new ASTNodeContainer(astID++, "finals");
        nodeContainerVars = new ASTNodeContainer(astID++, "vars");
        nodeContainerMethods = new ASTNodeContainer(astID++, "methods");
    }

    private void mergeContainers(){
        List<ASTNodeContainer> nodeContainers = new LinkedList<ASTNodeContainer>();
        nodeContainerFinal.setNodes(finalNodes);
        nodeContainerVars.setNodes(varNodes);
        nodeContainerMethods.setNodes(methodNodes);
        nodeContainers.add(nodeContainerFinal);
        nodeContainers.add(nodeContainerVars);
        nodeContainers.add(nodeContainerMethods);
        ast.setNodeContainers(nodeContainers);
    }

}
