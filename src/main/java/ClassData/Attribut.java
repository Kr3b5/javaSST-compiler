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

    private short start_pc;
    private short line_number;


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

    //LinenumberTable
    public Attribut(short nameIndex, int length, short countAttributes, List<Attribut> attributes) {
        this.nameIndex = nameIndex;
        this.length = length;
        this.countAttributes = countAttributes;
        this.attributes = attributes;
    }

    //LineNumberTableAttr
    public Attribut(short start_pc, short line_number) {
        this.start_pc = start_pc;
        this.line_number = line_number;
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

    public short getStart_pc() {
        return start_pc;
    }

    public short getLine_number() {
        return line_number;
    }

}
