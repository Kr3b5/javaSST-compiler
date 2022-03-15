package Class;

import AbstractSyntaxTree.ASTPrinter;
import ClassFile.ClassWriter;
import Helper.SemanticAnalyzer;
import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ClassTests {


    SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
    ASTPrinter printer = new ASTPrinter();

    // files
    private static final String CLASS_FILE_2        = "./src/test/resources/pass_test/ClassTest2.java";
    private static final String FSU_TEST            = "./src/test/resources/pass_test/Test.java";

    @Test
    public void FSUTest_complete() throws FileNotFoundException {
        runTest(FSU_TEST, true, false, false);
    }

    @Test
    public void ClassTest2() throws FileNotFoundException {
        runTest(CLASS_FILE_2, true, true, true);
    }


    private void runTest(String filePath, Boolean printAST, Boolean DebugModeSA, Boolean DebugModeCF) throws FileNotFoundException {
        Parser parser = new Parser(filePath);
        parser.parseFile();
        if(printAST) printer.printDot(parser.getAst());

        if(DebugModeSA) semanticAnalyzer.setDebugMode(true);
        boolean errorFound = semanticAnalyzer.analyze(parser.getAst(), parser.getSymbolTable());

        if(!errorFound){
            ClassWriter classWriter = new ClassWriter(parser.getAst());
            if(DebugModeCF) classWriter.setDebugMode(true);
            classWriter.genClass();
        }
    }

}
