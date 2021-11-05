package Scanner;

public enum Symbols {

    IDENT   (0),
    NUMBER  (1),
    LPAREN  (2),
    RPAREN  (3),
    PLUS    (4),
    MINUS   (5),
    TIMES   (6),
    OTHER   (99);


    public final int symbol;

    Symbols(int symbol) {
        this.symbol = symbol;
    }
}
