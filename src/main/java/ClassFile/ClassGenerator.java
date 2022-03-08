package ClassFile;

import AbstractSyntaxTree.AST;
import ClassData.*;
import Data.SymbolTable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private final short accessflags = 0x0001; // Public

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
    ByteBuffer buffer = ByteBuffer.allocate(65536); //TODO set to 65536
    private CPContainer cpc;

    //debug
    boolean debugMode;

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
        debugMode = false;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void genClass() {

        //generate ConstantPool
        CPGenerator cpGenerator =  new CPGenerator(ast);
        cpGenerator.setDebugMode(debugMode);
        cpc = cpGenerator.genConstantPool();
        constantPool = cpc.getConstantPool();




        code = genByteCode();

        if(debugMode)printByteCode();

        writeByteCodeToFile();
    }

    private void writeByteCodeToFile() {
        String filename = ast.getObject().getName() + ".class";
        try {
            FileUtils.writeByteArrayToFile(new File(filename), code);
            logger.info("Bytecode written to file: "+ filename );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    private byte[] genByteCode() {

        //u4 - add magic number
        insertInt(magicnumber);

        //u2 - minor
        insertShort(minor);
        //u2 - major
        insertShort(major);


        //u2 - constant_pool_count +  constant_pool
        insertShort((short)(constantPool.size() + 1));
        genByteCodeFromCP();

        //u2 - access_flags;
        insertShort(accessflags);
        //u2 - this_class;
        insertShort(cpc.getThis_class());
        //u2 - super_class;
        insertShort(cpc.getSuper_class());

        //u2 - interfaces count
        insertShort(countInterfaces);

        //TODO FIELDS

        //TODO MEHTODS

        //TODO ATTRIBUTES

        logger.info("Bytecode generated!");

        return buffer.array();
    }


    private void genByteCodeFromCP(){
        for (CPConstant c: constantPool.values()) {
            byte type = c.getType();
            /*
                u1 tag
                u2 name_index
             */
            if((byte) CPTypes.CLASS.value == type){
                insertByte(c.getType());
                insertShort(c.getBytefield1());
            }
            /*
                u1 tag
                u2 length
                u[length] value
             */
            else if((byte) CPTypes.UTF8.value == type){
                insertByte(c.getType());
                insertShort(c.getBytefield1());
                insertString(c.getsValue());
            }
            /*
                u1 tag
                u2 class_index | name_index
                u2 name_and_type_index | descriptor_index
             */
            else if((byte) CPTypes.FIELD.value == type ||
                     (byte) CPTypes.METHOD.value == type ||
                     (byte) CPTypes.NAMEANDTYPE.value == type ){
                insertByte(c.getType());
                insertShort(c.getBytefield1());
                insertShort(c.getBytefield2());
            }
            /*
                u1 tag
                u4 bytes
             */
            else if((byte) CPTypes.INTEGER.value == type){
                insertByte(c.getType());
                insertInt(c.getiValue());
            }else{
                logger.error("Error - Type not found!");
            }
        }
    }


    //-----------------------------------------------------------------------------------------------------------------

    void insertInt(int cp) {
        if (cur < 65535) {
            buffer.putInt(cp);
            cur += 4;
        } else {
            logger.error("code overflow");
        }
    }

    void insertShort(short cp) {
        if (cur < 65535) {
            buffer.putShort(cp);
            cur += 2;
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
            buffer.put(s.getBytes());
            cur = cur + s.length();
        } else {
            logger.error("code overflow");
        }
    }


    //-----------------------------------------------------------------------------------------------------------------

    private void printByteCode() {
        int i = 0;
        for (byte b : code) {
            if(i == 16){
                i = 0;
                System.out.print('\n');
            }
            System.out.format("%x ", b);
            i++;
        }
        System.out.print('\n');
        System.out.println(cur);
    }

}
