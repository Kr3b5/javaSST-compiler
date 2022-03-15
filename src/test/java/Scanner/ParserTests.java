package Scanner;

import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ParserTests {

    // normal cases
    private static final String FSU_TEST                = "./src/test/resources/pass_test/Test.java";

    // error cases
    private static final String E_MISSING_BRACKET_IF    = "./src/test/resources/error_case/Error_MissingBracketIf.jsst";
    private static final String E_MISSING_BRACKET_CLASS = "./src/test/resources/error_case/Error_MissingClassBracket.jsst";
    private static final String E_MISSING_ELSE          = "./src/test/resources/error_case/Error_MissingElse.jsst";
    private static final String E_MISSING_SEMI_METHOD   = "./src/test/resources/error_case/Error_MissingSemicolonInMethod.jsst";
    private static final String E_MISSING_STATEMENT     = "./src/test/resources/error_case/Error_MissingStatement.jsst";


    // normal tests
    @Test
    public void FSU_Test_complete(){ runOverFile(FSU_TEST); }

    // error tests
    @Test
    public void ErrorTest_MissBra_If(){ runOverFile(E_MISSING_BRACKET_IF); }

    @Test
    public void ErrorTest_MissBra_Class(){ runOverFile(E_MISSING_BRACKET_CLASS); }

    @Test
    public void ErrorTest_MissElse_If(){ runOverFile(E_MISSING_ELSE); }

    @Test
    public void ErrorTest_MissSemi(){ runOverFile(E_MISSING_SEMI_METHOD); }

    @Test
    public void ErrorTest_MissStatement(){ runOverFile(E_MISSING_STATEMENT); }

    private void runOverFile(String path){
        try {
            Parser parser = new Parser(path);
            parser.parseFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
