package ClassData;

/**
 * Instruction set - bytecode
 *
 * https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-6.html#jvms-6.5
 *
 * @author Kr3b5
 */
public enum InsSet {


    ALOAD_0                 ((byte) 0x2a),
    INVOKESPECIAL           ((byte) 0xb7),
    INVOKEVIRTUAL           ((byte) 0xb6),

    RETURN                  ((byte) 0xb1),
    IRETURN                 ((byte) 0xac),

    ICONST_0                ((byte) 0x3),
    ICONST_1                ((byte) 0x4),
    ICONST_2                ((byte) 0x5),
    ICONST_3                ((byte) 0x6),
    ICONST_4                ((byte) 0x7),
    ICONST_5                ((byte) 0x8),

    ISTORE                  ((byte) 0x36),

    ISTORE_0                ((byte) 0x3b),
    ISTORE_1                ((byte) 0x3c),
    ISTORE_2                ((byte) 0x3d),
    ISTORE_3                ((byte) 0x3e),

    ILOAD                   ((byte) 0x15),

    ILOAD_0                 ((byte) 0x1a),
    ILOAD_1                 ((byte) 0x1b),
    ILOAD_2                 ((byte) 0x1c),
    ILOAD_3                 ((byte) 0x1d),

    BIPUSH                  ((byte) 0x10),

    IADD                    ((byte) 0x60),
    ISUB                    ((byte) 0x64),
    IMUL                    ((byte) 0x68),
    IDIV                    ((byte) 0x6c),

    IFICMPEQ                ((byte) 0x9f),              // ==
    IFICMPNE                ((byte) 0xa0),              // !=
    IFICMPLT                ((byte) 0xa1),              // <
    IFICMPLE                ((byte) 0xa4),              // <=
    IFICMPGT                ((byte) 0xa3),              // >=
    IFICMPGE                ((byte) 0xa2),              // >=

    GOTO                    ((byte) 0xa7),

    GETFIELD                ((byte) 0xb4),
    PUTFIELD                ((byte) 0xb5);


    public final byte bytes;

    InsSet(byte bytes) {
        this.bytes = bytes;
    }
}
