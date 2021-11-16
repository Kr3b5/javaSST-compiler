package Data;

public class Token {

    private TokenType type;
    private Position position;

    private String value;

    public Token(TokenType type, String file, int line, int column) {
        this.type = type;
        this.position = new Position(file,line,column);
    }

    public Token(TokenType type, String file, int line, int column, String value) {
        this.type = type;
        this.position = new Position(file,line,column);
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
