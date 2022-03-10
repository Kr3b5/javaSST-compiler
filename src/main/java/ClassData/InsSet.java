package ClassData;

// https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-6.html#jvms-6.5

public enum InsSet {


    ALOAD_0                 ((byte) 0x2a),
    INVOKESPECIAL           ((byte) 0xb7),
    RETURN                  ((byte) 0xb1),

    ICONST_0                ((byte) 0x3),
    ICONST_1                ((byte) 0x4),
    ICONST_2                ((byte) 0x5),
    ICONST_3                ((byte) 0x6),
    ICONST_4                ((byte) 0x7),
    ICONST_5                ((byte) 0x8),

    BIPUSH                  ((byte) 0x10),

    PUTFIELD                ((byte) 0xb5);



    public final byte bytes;

    InsSet(byte bytes) {
        this.bytes = bytes;
    }
}
