package ClassFile;

import AST.AST;
import ClassData.Field;
import ClassData.Method;
import Data.SymbolTable;
import Helper.SemanticAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ClassGenerator {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(ClassGenerator.class.getName());

    // classfile
    private final int magicnumber = 0xCAFEBABE;
    private final short major     = 59;                     // Java 8 = 52 (0x34)| Java 15 = 59 (0x3B)
    private final short minor     = 0;

    private short countConstantPool;
    private HashMap<String, Integer> constantPool;

    private final short accessflags   = 0; //TODO
    private final short normalClass   = 0;
    private final short superClass    = 0;

    private final short countInterfaces    = 0;   // Interfaces not allowed
    // interface table not used

    private short countFields;
    private List<Field> fields;

    private short countMethods;
    private List<Method> methods;

    private short countAttributes;
    private List<Field> attributes;

    // globals
    private AST ast;
    private SymbolTable symbolTable;

    private int cur;
    private byte[] code;

    //constructor
    public ClassGenerator(AST ast, SymbolTable symbolTable) {
        this.ast = ast;
        this.symbolTable = symbolTable;
        code = new byte[65536];
        cur = 0;
        countConstantPool = 0;
        constantPool = new HashMap<>();
        countFields = 0;
        fields = new LinkedList<>();
        countMethods = 0;
        methods = new LinkedList<>();
        countAttributes = 0;
        attributes = new LinkedList<>();
    }



    public void genClass(){



    }


    void insertInteger(int cp) {
        if (cur < 65535)
            code[cur++] = (byte) cp;
        else{
            logger.error("code overflow");
        }
    }

    void insertShort(short cp) {
        if (cur < 65535)
            code[cur++] = (byte) cp;
        else{
            logger.error("code overflow");
        }
    }

}
