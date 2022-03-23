package ClassData;

import java.util.List;

/**
 * code method
 *
 * https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.6
 *
 * @author Kr3b5
 */
public class Method extends Entry{
    public Method(short accessFlag, short nameIndex, short signatureIndex, short countAttributes, List<Attribut> attributes) {
        super(accessFlag, nameIndex, signatureIndex, countAttributes, attributes);
    }
}
