package Data;

public enum ObjClass {

    CLASS   (1),
    PAR     (2),
    CONST   (3),
    PROC    (4),
    VAR     (5);


    /**
     * The ID of the Class
     */
    public final int id;

    ObjClass(int id) {
        this.id = id;
    }
}
