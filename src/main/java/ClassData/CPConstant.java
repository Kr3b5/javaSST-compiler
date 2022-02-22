package ClassData;

public class CPConstant {
    byte type;
    short bytefield1;
    short bytefield2;

    String sValue;
    int iValue;

    //Method, Name, NaT
    public CPConstant(byte type, short bytefield1, short bytefield2) {
        this.type = type;
        this.bytefield1 = bytefield1;
        this.bytefield2 = bytefield2;
    }

    //UTF-8
    public CPConstant(byte type, short bytefield1, String sValue) {
        this.type = type;
        this.bytefield1 = bytefield1;
        this.sValue = sValue;
    }

    //Integer
    public CPConstant(byte type, int iValue) {
        this.type = type;
        this.iValue = iValue;
    }

    //Class
    public CPConstant(byte type, short bytefield1) {
        this.type = type;
        this.bytefield1 = bytefield1;
    }

    public byte getType() {
        return type;
    }

    public short getBytefield1() {
        return bytefield1;
    }

    public short getBytefield2() {
        return bytefield2;
    }

    public String getsValue() {
        return sValue;
    }

    public int getiValue() {
        return iValue;
    }
}
