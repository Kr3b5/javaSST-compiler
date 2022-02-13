package ClassData;

import java.util.List;

public class Method extends Entry{
    public Method(short accessFlag, short nameIndex, short signatureIndex, short countAttributes, List<Attribut> attributes) {
        super(accessFlag, nameIndex, signatureIndex, countAttributes, attributes);
    }
}
