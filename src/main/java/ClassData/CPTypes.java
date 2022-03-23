package ClassData;

/**
 * Constantpool types
 *
 * https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.4-140
 *
 * @author Kr3b5
 */
public enum CPTypes {

    UTF8                ((short) 1),
    INTEGER             ((short) 3),
    CLASS               ((short) 7),
    FIELD               ((short) 9),
    METHOD              ((short) 10),
    NAMEANDTYPE         ((short) 12);

    public final short value;

    CPTypes(short value) {
        this.value = value;
    }

}
