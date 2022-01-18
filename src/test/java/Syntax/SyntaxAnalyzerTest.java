package Syntax;

import AST.ASTPrinter;
import Helper.SemanticAnalyzer;
import Parser.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class SyntaxAnalyzerTest {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(SyntaxAnalyzerTest.class.getName());


    SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
    ASTPrinter printer = new ASTPrinter();

    // files
    private static final String FSU_TEST            = "./src/test/resources/pass_test/Test.java";

    private static final String RETURN_TEST         = "./src/test/resources/semantic_test/returnTest.java";
    private static final String IFWHILE_TEST        = "./src/test/resources/semantic_test/ifwhileBoolTest.java";
    private static final String VAR_TEST            = "./src/test/resources/semantic_test/initVarTest.java";
    private static final String FINAL_TEST            = "./src/test/resources/semantic_test/finalAssignTest.java";


    @Test
    public void FSUTest_complete() throws FileNotFoundException {
        Parser parser = new Parser(FSU_TEST);
        parser.parseFile();
        //printer.printDot(parser.getAst());
        logger.info("Error found: " + semanticAnalyzer.analyze(parser.getAst(), parser.getSymbolTable()));
    }

    @Test
    public void RETURN_Test() throws FileNotFoundException {
        Parser parser = new Parser(RETURN_TEST);
        parser.parseFile();
        //printer.printDot(parser.getAst());

        semanticAnalyzer.setDebugMode(true);
        logger.error("Error found: " + semanticAnalyzer.analyze(parser.getAst(), parser.getSymbolTable()));
    }

    @Test
    public void IFWHILEBool_Test() throws FileNotFoundException {
        Parser parser = new Parser(IFWHILE_TEST);
        parser.parseFile();
        //printer.printDot(parser.getAst());

        semanticAnalyzer.setDebugMode(true);
        logger.error("Error found: " + semanticAnalyzer.analyze(parser.getAst(), parser.getSymbolTable()));
    }

    @Test
    public void VAR_Test() throws FileNotFoundException {
        Parser parser = new Parser(VAR_TEST);
        parser.parseFile();
        //printer.printDot(parser.getAst());

        semanticAnalyzer.setDebugMode(true);
        logger.error("Error found: " + semanticAnalyzer.analyze(parser.getAst(), parser.getSymbolTable()));
    }

    @Test
    public void FINALASSIGN_Test() throws FileNotFoundException {
        Parser parser = new Parser(FINAL_TEST);
        parser.parseFile();
        //printer.printDot(parser.getAst());

        semanticAnalyzer.setDebugMode(true);
        logger.error("Error found: " + semanticAnalyzer.analyze(parser.getAst(), parser.getSymbolTable()));
    }

}
