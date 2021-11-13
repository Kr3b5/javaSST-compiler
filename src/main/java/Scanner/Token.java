package Scanner;

public class Token {

    private Symbols symbol;
    private int position;

    public Token(Symbols symbol, int position) {
        this.symbol = symbol;
        this.position = position;
    }

    public Symbols getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbols symbol) {
        this.symbol = symbol;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
