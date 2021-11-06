package Scanner;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ScannerTests {

    private static final String TEST_FILE = "./src/test/java/Testfiles/HelloWorld.java";

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

                System.out.println( "Sym-ID:" + scanner.sym +
                                    " | Sym.ID/NUM: " + output );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }




}
