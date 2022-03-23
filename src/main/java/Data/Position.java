package Data;

/**
 * File position object
 *
 * @author Kr3b5
 */
public class Position {

    private String filename;
    private int line;
    private int column;

    public Position(String filename, int line, int column) {
        this.filename = filename;
        this.line = line;
        this.column = column;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getLine() {
        return line;
    }
    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }
    public void setColumn(int column) {
        this.column = column;
    }
}
