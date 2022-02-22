package ClassFile;

import AbstractSyntaxTree.AST;
import ClassData.CPConstant;
import ClassData.Field;
import ClassData.Method;
import Data.SymbolTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class ClassGenerator {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(ClassGenerator.class.getName());

    // classfile
    private final int magicnumber = 0xCAFEBABE;
    private final short major = 59;                     // Java 8 = 52 (0x34)| Java 15 = 59 (0x3B)
    private final short minor = 0;

    private short countConstantPool;
    private HashMap<Short, CPConstant> constantPool;

    private final short accessflags = 0; //TODO
    private final short normalClass = 0;
    private final short superClass = 0;

    private final short countInterfaces = 0;   // Interfaces not allowed
    // interface table not used

    private short countFields;
    private List<Field> fields;

    private short countMethods;
    private List<Method> methods;

    private short countAttributes;
    private List<Field> attributes;

    // globals
    private final AST ast;
    private final SymbolTable symbolTable;

    private int cur;
    private byte[] code;
    ByteBuffer buffer = ByteBuffer.allocate(100); //TODO set to 65536

    //constructor
    public ClassGenerator(AST ast, SymbolTable symbolTable) {
        this.ast = ast;
        this.symbolTable = symbolTable;
        //code = new byte[65536];
        cur = 0;
        countConstantPool = 1;
        constantPool = new HashMap<>();
        countFields = 0;
        fields = new LinkedList<>();
        countMethods = 0;
        methods = new LinkedList<>();
        countAttributes = 0;
        attributes = new LinkedList<>();
    }


    public void genClass() {
        CPGenerator cpGenerator =  new CPGenerator(ast);
        constantPool = cpGenerator.genConstantPool();


        code = genByteCode();

        for (byte b : code) {
            //if ( b != 0 )
            System.out.format("%x ", b);
        }
        System.out.print('\n');
        System.out.println(cur);

    }

    //-----------------------------------------------------------------------------------------------------------------

    private byte[] genByteCode() {

        // add magic number
        insertInt(magicnumber);

        //add major and minor
        insertShort(major);
        insertShort(minor);

        System.out.println(constantPool.size());

        //TODO Test FF
        buffer.put((byte) 0xFF);
        return buffer.array();
    }

    //-----------------------------------------------------------------------------------------------------------------

    void insertInt(int cp) {
        if (cur < 65535) {
            buffer.putInt(cp);
            cur = cur + 4;
        } else {
            logger.error("code overflow");
        }
    }

    void insertShort(short cp) {
        if (cur < 65535) {
            buffer.putShort(cp);
            cur++;
        } else {
            logger.error("code overflow");
        }
    }

    void insertByte(byte cp) {
        if (cur < 65535) {
            buffer.put(cp);
            cur++;
        } else {
            logger.error("code overflow");
        }
    }

    void insertString(String s) {
        if (cur < 65535) {
            for (int i = 0; i < s.length(); i++) {
                buffer.putChar(s.charAt(i));
            }
        } else {
            logger.error("code overflow");
        }
    }


    //-----------------------------------------------------------------------------------------------------------------


}
