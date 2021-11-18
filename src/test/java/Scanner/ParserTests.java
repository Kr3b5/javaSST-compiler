package Scanner;

import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ParserTests {

    // normal cases
    private static final String COMPLETE_TEST = "./src/test/resources/pass_test/CompleteTest.jsst";
    private static final String EMPTY_CLASS_TEST = "./src/test/resources/pass_test/EmptyClass.jsst";

    // error cases
    private static final String BRACKET_MISSING_TEST = "./src/test/resources/error_case/Error_MissingClassBracket.jsst";

    @Test
    public void NormalTest_complete(){
        runOverFile(COMPLETE_TEST);
    }

    @Test
    public void NormalTest_EmptyClass(){
        runOverFile(EMPTY_CLASS_TEST);
    }

    @Test
    public void ErrorTest_BracketMissing(){
        runOverFile(BRACKET_MISSING_TEST);
    }

    private void runOverFile(String path){
        try {
            Parser parser = new Parser(path);
            parser.parseFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
