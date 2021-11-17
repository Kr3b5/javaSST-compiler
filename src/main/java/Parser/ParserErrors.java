package Parser;

public enum ParserErrors {

    ERROR_CLASS                 ("No class defined"),
    ERROR_IDENT                 ("No ident found"),
    ERROR_LCBRACKET             ("\"{\" is missing!"),
    ERROR_RCBRACKET             ("\"}\" is missing!"),
    ERROR_INT                   ("\"int\" is missing!"),
    ERROR_SEMI                  ("\";\" is missing!"),
    ERROR_ASSIGN                ("\"=\" is missing!"),
    ERROR_FINAL                 ("\"final\" is missing!"),
    ERROR_PUBLIC                ("\"public\" is missing!"),
    ERROR_METHOD_TYPE           ("Methodtype (void / int) is missing!"),
    ERROR_LPAREN                ("\"(\" is missing!"),
    ERROR_RPAREN                ("\")\" is missing!"),
    ERROR_STATEMENT             ("Statement is missing!"),
    ERROR_ELSE                  ("\"else\" is missing!"),
    ERROR_SIMPLE_EXP            ("Simple expression is missing!"),
    ERROR_FACTOR                ("Error in factor expression!"),

    ERROR_EOF                   ("File not complete");





    public final String message;

    ParserErrors(String message) {
        this.message = message;
    }
}
