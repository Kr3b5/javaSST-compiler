package Scanner;

/**
 * Enum of symbols
 * @author Kr3b5
 */
public enum Symbols {

    IDENT       (0),        // ident
    NUMBER      (1),        // number
    LPAREN      (2),        // (
    RPAREN      (3),        // )
    PLUS        (4),        // +
    MINUS       (5),        // -
    TIMES       (6),        // *
    SLASH       (7),        // /
    LBRACKET    (8),        // [
    RBRACKET    (9),        // ]
    COMMA       (10),       // ,
    SEMI        (11),       // ;
    ASSIGN      (12),       // =
    EQUAL       (13),       // ==
    NEQUAL      (14),       // !=
    GREATER     (15),       // >
    SMALLER     (16),       // <
    GR_EQ       (17),       // >=
    SM_EQ       (18),       // <=
    OTHER       (99);


    /**
     * The ID of the symbols
     */
    public final int id;

    Symbols(int symbol) {
        this.id = symbol;
    }
}
