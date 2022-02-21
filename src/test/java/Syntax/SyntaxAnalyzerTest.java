package Syntax;

import AbstractSyntaxTree.ASTPrinter;
import Helper.SemanticAnalyzer;
import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class SyntaxAnalyzerTest {

    SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
    ASTPrinter printer = new ASTPrinter();

    // files
    private static final String FSU_TEST            = "./src/test/resources/pass_test/Test.java";

    private static final String RETURN_TEST         = "./src/test/resources/semantic_test/returnTest.java";
    private static final String IFWHILE_TEST        = "./src/test/resources/semantic_test/ifwhileBoolTest.java";
    private static final String VAR_TEST            = "./src/test/resources/semantic_test/initVarTest.java";
    private static final String FINAL_TEST          = "./src/test/resources/semantic_test/finalAssignTest.java";


    @Test
    public void FSUTest_complete() throws FileNotFoundException {
        runTest(FSU_TEST, false, false);
    }

    @Test
    public void RETURN_Test() throws FileNotFoundException {
        runTest(RETURN_TEST, false, true);
    }

    @Test
    public void IFWHILEBool_Test() throws FileNotFoundException {
        runTest(IFWHILE_TEST, false, true);
    }

    @Test
    public void VAR_Test() throws FileNotFoundException {
        runTest(VAR_TEST, false, true);
    }

    @Test
    public void FINALASSIGN_Test() throws FileNotFoundException {
        runTest(FINAL_TEST, false, true);
    }


    private void runTest(String filePath, Boolean printAST, Boolean DebugMode) throws FileNotFoundException {
        Parser parser = new Parser(filePath);
        parser.parseFile();
        if(printAST) printer.printDot(parser.getAst());

        if(DebugMode) semanticAnalyzer.setDebugMode(true);
        semanticAnalyzer.analyze(parser.getAst(), parser.getSymbolTable());
    }
}
