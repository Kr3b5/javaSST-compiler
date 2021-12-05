package Data;

public class STObject {

    private final String name;
    private final int objClass;
    private STType STType;

    private int intValue;


    private STObject parameterList;
    private STObject varList;

    private STObject varDef;
    private STObject methodDef;

    private SymbolTable symtab;

    //CLASS
    public STObject(String name, int objClass, STObject varDef, STObject methodDef, SymbolTable symtab) {
        this.name = name;
        this.objClass = objClass;
        this.varDef = varDef;
        this.methodDef = methodDef;
        this.symtab = symtab;
    }

    // VAR & PAR
    public STObject(String name, int objClass, STType STType) {
        this.name = name;
        this.objClass = objClass;
        this.STType = STType;
    }

    // CONS
    public STObject(String name, int objClass, STType STType, int intValue) {
        this.name = name;
        this.objClass = objClass;
        this.STType = STType;
        this.intValue = intValue;
    }

    // METHOD
    public STObject(String name, int objClass, STType resultType, STObject parameterList, STObject varList, SymbolTable symtab) {
        this.name = name;
        this.objClass = objClass;
        this.STType = resultType;
        this.parameterList = parameterList;
        this.varList = varList;
        this.symtab = symtab;
    }


    public String getName() {
        return name;
    }

    public SymbolTable getSymtab() {
        return symtab;
    }

    @Override
    public String toString() {
        return "STObject{" +
                "name='" + name + '\'' +
                ", objClass=" + objClass +
                ", type=" + STType +
                ", intValue=" + intValue +
                ", parameterListe=" + parameterList +
                ", varListe=" + varList +
                ", varDef=" + varDef +
                ", methodDef=" + methodDef +
                ", symtab=" + symtab +
                '}';
    }
}
