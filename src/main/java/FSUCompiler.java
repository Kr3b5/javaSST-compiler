import AbstractSyntaxTree.ASTPrinter;
import ClassFile.ClassWriter;
import Helper.SemanticAnalyzer;
import Parser.Parser;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class FSUCompiler {

    private static boolean debugMode;
    private static boolean printDotMode;


    public static void main(String[] args) {
        ArgCheck(args);
        start(args[0]);
    }

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


    private static void ArgCheck(String[] args){
        if(args.length < 1 || !args[0].contains(".java") || args.length > 3){
            printHelp();
            System.exit(1);
        }

        debugMode = Arrays.asList(args).contains("-debug");
        printDotMode = Arrays.asList(args).contains("-dot");
    }

    private static void printHelp(){
        System.out.println("USAGE: FSUCompile-1.0.jar <java-file> <options>");
        System.out.println("Valid Options:");
        System.out.println("    -dot : Print Dot File");
        System.out.println("    -debug: print Debug");
    }


}
