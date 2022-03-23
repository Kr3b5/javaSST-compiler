package Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.Iterator;

/**
 * Iterator for reading the file
 * @author Kr3b5
 */
public class Input implements Iterator<Character> {

    /** The logger for this class. */
    private static final Logger logger = LogManager.getLogger(Input.class.getName());

    private static final int EOF    = -1;
    private static final int ERROR  = -2;

    private final BufferedReader reader;
    private int buffer;

    /**
     * Instantiates new Input.
     *
     * @param filename the filename
     * @throws FileNotFoundException Exception - file not found
     */
    public Input(final String filename) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(filename));
        next(); //read first char in buffer
    }

    /**
     * Read new char in buffer and return old buffer
     *
     * @return char in buffer
     */
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

    /**
     * Checks if file has a next char
     *
     * @return boolean if file has next char
     */
    @Override
    public boolean hasNext() {
        return buffer != EOF && buffer != ERROR;
    }

    /**
     * Return the buffer.
     *
     * @return char in buffer
     */
    public Character getBuffer() {
        return (char) this.buffer;
    }
}
