package Parser;

public enum ParserErrors {

    ERROR_CLASS                 ("No class defined"),
    ERROR_IDENT                 ("No ident found"),
    ERROR_LCBRACKET             ("\"{\" is missing!"),
    ERROR_RCBRACKET             ("\"}\" is missing!"),


    ERROR_EOF                   ("File not complete");





    public final String message;

    ParserErrors(String message) {
        this.message = message;
    }
}
