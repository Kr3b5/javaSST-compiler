package Scanner;

import Data.STObject;
import Data.SymbolTable;
import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class SymbolTableTest {

    // normal cases
    private static final String COMPLETE_TEST           = "./src/test/resources/pass_test/CompleteTest.jsst";
    private static final String EMPTY_CLASS_TEST        = "./src/test/resources/pass_test/EmptyClass.jsst";
    private static final String FSU_TEST                = "./src/test/resources/pass_test/Test.java";

    // normal tests
    @Test
    public void NormalTest_complete(){ runOverFile(COMPLETE_TEST); }

    private void runOverFile(String path){
        try {
            Parser parser = new Parser(path);
            parser.parseFile();
            SymbolTable symbolTable = parser.getSymbolTable();
            //CLASS
            for (STObject Cobj : symbolTable.getObjects()) {
                System.out.println(Cobj.toString());
                if( Cobj.getSymtab() != null ){
                    //CLASSBODY
                    SymbolTable CBsymbolTable = Cobj.getSymtab();
                    for (STObject CBobj : CBsymbolTable.getObjects()) {
                        System.out.println("   " + CBobj.toString());
                        if( CBobj.getSymtab() != null ){
                            //METHODBODY
                            SymbolTable MBsymbolTable = CBobj.getSymtab();
                            for (STObject MBobj : MBsymbolTable.getObjects()) {
                                System.out.println("      " + MBobj.toString());
                            }
                        }

                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
