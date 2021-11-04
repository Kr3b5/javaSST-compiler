package Scanner;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

public class InputTests {

    private static final String TEST_FILE = "./src/test/java/Testfiles/HelloWorld.java";

    @Test
    public void Run_without_error() {
        try {
            Input i = new Input(TEST_FILE);
            while (i.hasNext()) {
                System.out.print(i.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
