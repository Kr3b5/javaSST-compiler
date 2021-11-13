package Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ScannerTests {

    /** The logger for this class. */
    private static final Logger logger = LogManager.getLogger(ScannerTests.class.getName());

    private static final String TEST_FILE = "./src/test/java/Testfiles/HelloWorld.jsst";

    @Test
    public void RunOverTestfile(){
        try {
            Input input = new Input(TEST_FILE);
            Scanner scanner = new Scanner(input);
            while (input.hasNext()){
                scanner.getSym();

                String output = null;
                if(!scanner.id.equals("")) {
                    output = scanner.id;
                }else if(!scanner.num.equals("")){
                    output = scanner.num;
                }

                //TODO: SAY OK ?
                if(scanner.sym != 99){
                    System.out.println( "OK - " + scanner.sym + " | " + output );
                }else if(scanner.sym == 99){
                    System.out.println( "Unknown Sym - " + scanner.sym + " | " + output );
                }else{
                    System.out.println( "NOT OK!" );
                }

            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
    }




}
