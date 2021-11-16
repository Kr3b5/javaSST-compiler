package Data;

/**
 * Enum of symbols
 * @author Kr3b5
 */
public enum TokenType {

    // terminals
    IDENT       (0),        // ident
    NUMBER      (1),        // number
    LPAREN      (2),        // (
    RPAREN      (3),        // )
    PLUS        (4),        // +
    MINUS       (5),        // -
    TIMES       (6),        // *
    SLASH       (7),        // /
    COMMA       (8),       // ,
    SEMI        (9),       // ;
    ASSIGN      (10),       // =
    EQUAL       (11),       // ==
    NEQUAL      (12),       // !=
    GREATER     (13),       // >
    SMALLER     (14),       // <
    GR_EQ       (15),       // >=
    SM_EQ       (16),       // <=
    LCBRACKET   (17),       // {
    RCBRACKET   (18),       // }

    // keywords
    CLASS       (30),
    PUBLIC      (31),
    FINAL       (32),
    VOID        (33),
    INT         (34),
    IF          (35),
    ELSE        (36),
    WHILE       (37),
    RETURN      (38),

    // other
    OTHER       (99),
    EOF         (100);


    /**
     * The ID of the symbols
     */
    public final int id;

    TokenType(int symbol) {
        this.id = symbol;
    }
}
