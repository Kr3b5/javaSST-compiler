package ClassData;

import java.util.List;

public class Field extends Entry {
    public Field(short accessFlag, short nameIndex, short signatureIndex, short countAttributes, List<Attribut> attributes) {
        super(accessFlag, nameIndex, signatureIndex, countAttributes, attributes);
    }
}
