package Scanner;

import Data.STObject;
import Data.SymbolTable;
import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class SymbolTableTest {

    // files
    private static final String FSU_TEST                = "./src/test/resources/pass_test/Test.java";

    // tests
    @Test
    public void FSUTest_complete(){ runOverFile(FSU_TEST); }

    private void runOverFile(String path){
        try {
            Parser parser = new Parser(path);
            parser.parseFile();

            System.out.println("");
            System.out.println("------------------------------------------SYMBOLTABLE------------------------------------------");
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
            System.out.println("-----------------------------------------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
