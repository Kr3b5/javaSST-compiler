package Syntax;

import AST.ASTPrinter;
import Helper.SemanticAnalyzer;
import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class SyntaxAnalyzerTest {

    SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
    ASTPrinter printer = new ASTPrinter();

    // files
    private static final String FSU_TEST            = "./src/test/resources/pass_test/Test.java";
    private static final String TEST                = "./src/test/resources/pass_test/TestSemanticAnalyzer.java";

    @Test
    public void FSUTest_complete() throws FileNotFoundException {
        Parser parser = new Parser(FSU_TEST);
        parser.parseFile();

        //printer.printDot(parser.getAst());

        System.out.println("Error found: " + semanticAnalyzer.analyze(parser.getAst()));
    }

    @Test
    public void Test() throws FileNotFoundException {
        Parser parser = new Parser(TEST);
        parser.parseFile();

        //printer.printDot(parser.getAst());

        System.out.println("Error found: " + semanticAnalyzer.analyze(parser.getAst()));
    }


}
