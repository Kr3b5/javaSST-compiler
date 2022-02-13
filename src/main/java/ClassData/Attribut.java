package ClassData;

import java.util.List;

public class Attribut {

    private short nameIndex;
    private int length;
    private short Index;
    private short stackSize;
    private short countLocalVars;
    private int codeLength;
    private byte[] code;

    private final short countExceptionTable = 0;
    //Exception not allowed

    private short countAttributes;
    private List<Attribut> attributes;


    // Class + Fields
    public Attribut(short nameIndex, int length, short index) {
        this.nameIndex = nameIndex;
        this.length = length;
        Index = index;
    }

    // Methods
    public Attribut(short nameIndex, int length, short stackSize, short countLocalVars, int codeLength, byte[] code, short countAttributes, List<Attribut> attributes) {
        this.nameIndex = nameIndex;
        this.length = length;
        this.stackSize = stackSize;
        this.countLocalVars = countLocalVars;
        this.codeLength = codeLength;
        this.code = code;
        this.countAttributes = countAttributes;
        this.attributes = attributes;
    }








    // GETTER

    public short getNameIndex() {
        return nameIndex;
    }

    public int getLength() {
        return length;
    }

    public short getIndex() {
        return Index;
    }

    public short getStackSize() {
        return stackSize;
    }

    public short getCountLocalVars() {
        return countLocalVars;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public byte[] getCode() {
        return code;
    }

    public short getCountExceptionTable() {
        return countExceptionTable;
    }

    public short getCountAttributes() {
        return countAttributes;
    }

    public List<Attribut> getAttributes() {
        return attributes;
    }

}
