package Scanner;

import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ParserTests {

    private static final String COMPLETE_TEST = "./src/test/resources/pass_test/CompleteTest.jsst";
    private static final String EMPTY_CLASS_TEST = "./src/test/resources/pass_test/EmptyClass.jsst";

    @Test
    public void CompleteTest(){
        runOverFile(COMPLETE_TEST);
    }

    @Test
    public void EmptyClassTest(){
        runOverFile(EMPTY_CLASS_TEST);
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
