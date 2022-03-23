import AbstractSyntaxTree.ASTPrinter;
import ClassFile.ClassWriter;
import Helper.SemanticAnalyzer;
import Parser.Parser;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * FSU Compiler
 *
 * @author Kr3b5
 */
public class FSUCompiler {

    private static boolean debugMode;
    private static boolean printDotMode;

    /**
     * main
     * @param args arguments
     */
    public static void main(String[] args) {
        ArgCheck(args);
        start(args[0]);
    }

    /**
     * start compiler
     * @param filepath path java file
     */
    private static void start(String filepath){
        try {
            Parser parser = new Parser(filepath);
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            ASTPrinter printer = new ASTPrinter();

            parser.parseFile();
            if(printDotMode) printer.printDot(parser.getAst());

            if(debugMode) semanticAnalyzer.setDebugMode(true);
            boolean errorFound = semanticAnalyzer.analyze(parser.getAst(), parser.getSymbolTable());

            if(!errorFound){
                ClassWriter classWriter = new ClassWriter(parser.getAst());
                if(debugMode) classWriter.setDebugMode(true);
                classWriter.genClass();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * check arguments
     * @param args arguments array
     */
    private static void ArgCheck(String[] args){
        if(args.length < 1 || !args[0].contains(".java") || args.length > 3){
            printHelp();
            System.exit(1);
        }

        debugMode = Arrays.asList(args).contains("-debug");
        printDotMode = Arrays.asList(args).contains("-dot");
    }

    /**
     * print help
     */
    private static void printHelp(){
        System.out.println("USAGE: FSUCompile-1.0.jar <java-file> <options>");
        System.out.println("Valid Options:");
        System.out.println("    -dot : Print Dot File");
        System.out.println("    -debug: print Debug");
    }


}
