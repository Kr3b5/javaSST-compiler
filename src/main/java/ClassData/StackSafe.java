package ClassData;

/**
 * safe for stored variables - bytecodegen
 *
 * @author Kr3b5
 */
public class StackSafe {

    int index;
    String var;

    public StackSafe(int index, String var) {
        this.index = index;
        this.var = var;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
