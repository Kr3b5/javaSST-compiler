package ClassData;

import java.util.List;

/**
 * code field
 *
 * https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.5
 *
 * @author Kr3b5
 */
public class Field extends Entry {
    public Field(short accessFlag, short nameIndex, short signatureIndex, short countAttributes, List<Attribut> attributes) {
        super(accessFlag, nameIndex, signatureIndex, countAttributes, attributes);
    }
}
