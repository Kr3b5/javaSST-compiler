package Parser;

public enum ParserErrors {

    ERROR_CLASS                 ("No class defined"),
    ERROR_IDENT                 ("No ident found"),
    ERROR_LCBRACKET             ("\"{\" is missing!"),
    ERROR_RCBRACKET             ("\"}\" is missing!"),
    ERROR_TYPE                  ("Type \"int\" is missing!"),
    ERROR_SEMI                  ("\";\" is missing!"),
    ERROR_ASSIGN                ("\"=\" is missing!"),
    ERROR_PUBLIC                ("\"public\" is missing!"),
    ERROR_METHOD_TYPE           ("Methodtype (void / int) is missing!"),
    ERROR_LPAREN                ("\"(\" is missing!"),
    ERROR_RPAREN                ("\")\" is missing!"),
    ERROR_STATEMENT             ("Statement is missing!"),
    ERROR_ELSE                  ("\"else\" is missing!"),
    ERROR_FACTOR                ("Ident or Number is missing!"),

    ERROR_EMPTY                 ("File is empty!"),
    ERROR_EOF                   ("File not complete"),

    NO_ERROR                    ("No Error found!");





    public final String message;

    ParserErrors(String message) {
        this.message = message;
    }
}
