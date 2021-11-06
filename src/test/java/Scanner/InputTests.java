package Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

public class InputTests {

    /** The logger for this class. */
    private static final Logger logger = LogManager.getLogger(InputTests.class.getName());

    private static final String TEST_FILE = "./src/test/java/Testfiles/HelloWorld.java";

    @Test
    public void Run_without_error() {
        try {
            Input i = new Input(TEST_FILE);
            while (i.hasNext()) {
                System.out.print(i.next());
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
    }
}
