package Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ScannerTests {

    /** The logger for this class. */
    private static final Logger logger = LogManager.getLogger(ScannerTests.class.getName());

    private static final String TEST_FILE = "./src/test/java/Testfiles/HelloWorld.jsst";

    private Scanner scanner;

    @Test
    public void RunOverTestfile(){
        try {
            scanner = new Scanner(TEST_FILE);
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
