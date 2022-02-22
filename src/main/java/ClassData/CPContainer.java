package ClassData;

import java.util.HashMap;

public class CPContainer {
    private HashMap<Short, CPConstant> constantPool;
    private short super_class;
    private short this_class;

    public CPContainer(HashMap<Short, CPConstant> constantPool, short super_class, short this_class) {
        this.constantPool = constantPool;
        this.super_class = super_class;
        this.this_class = this_class;
    }

    public HashMap<Short, CPConstant> getConstantPool() {
        return constantPool;
    }

    public short getSuper_class() {
        return super_class;
    }

    public short getThis_class() {
        return this_class;
    }
}
