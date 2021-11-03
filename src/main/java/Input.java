import java.io.*;

public class Input {

    private FileInputStream fis;

    public Input(File f){
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public char next(){
        try {
            return (char)fis.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
