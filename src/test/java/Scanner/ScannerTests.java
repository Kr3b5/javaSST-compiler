package Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ScannerTests {

    /** The logger for this class. */
    private static final Logger logger = LogManager.getLogger(ScannerTests.class.getName());

    private Scanner scanner;


    private static final String COMPLETE_TEST = "./src/test/resources/pass_test/CompleteTest.jsst";
    private static final String EMPTY_CLASS_TEST = "./src/test/resources/pass_test/EmptyClass.jsst";

    @Test
    public void completeTest(){
        runOverFile(COMPLETE_TEST);
    }

    @Test
    public void emptyClassTest(){
        runOverFile(EMPTY_CLASS_TEST);
    }


    public void runOverFile(String Path){
        try {
            scanner = new Scanner(Path);
            while (scanner.hasNext()){
                scanner.getSym();
                printOutput(scanner.getValue());
            }
            printOutput(scanner.getValue());
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

    private void printOutput(String value){
        String out = scanner.getToken().getPosition().getFilename()    + "("
                + scanner.getToken().getPosition().getLine()      + ","
                + scanner.getToken().getPosition().getColumn()    + "): "
                + scanner.getToken().getType().name();
        if(!value.equals("")) out = out + "[" + value + "]";
        System.out.println( out );
    }



}
