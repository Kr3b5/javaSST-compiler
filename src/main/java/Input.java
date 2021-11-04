import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.Iterator;


public class Input implements Iterator<Character> {

    /** The logger for this class. */
    private static final Logger logger = LogManager.getLogger(Input.class.getName());

    private static final int EOF    = -1;
    private static final int ERROR  = -2;

    private final BufferedReader reader;
    private int buffer;

    public Input(final String filename) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(filename));
        next(); //read first char in buffer
    }

    @Override
    public Character next() {
        int out = buffer;
        try {
            buffer = reader.read();
        } catch (IOException e) {
            buffer = ERROR;
            logger.error(e.getMessage());
        }
        return (char) out;
    }

    @Override
    public boolean hasNext() {
        return buffer != EOF && buffer != ERROR;
    }
}
