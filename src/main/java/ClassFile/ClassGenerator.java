package ClassFile;

import AbstractSyntaxTree.AST;
import ClassData.*;
import Data.SymbolTable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

// https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html

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

    private short this_class;
    private short super_class;

    private HashMap<Short, CPConstant> constantPool;

    private final short accessflags = 0x0001; // Public

    private final short countInterfaces = 0;   // Interfaces not allowed
    // interface table not used

    private List<Field> fields;

    private List<Method> methods;

    private List<Field> attributes;


    // globals
    private final AST ast;

    private byte[] code;
    ByteBuffer buffer = ByteBuffer.allocate(65536); //TODO
    private short sourcefile;

    //debug
    boolean debugMode;

    //constructor
    public ClassGenerator(AST ast) {
        this.ast = ast;
        constantPool = new HashMap<>();
        fields = new LinkedList<>();
        methods = new LinkedList<>();
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
        cpGenerator.generate();
        getCPValues(cpGenerator);

        code = genByteCode();

        if(debugMode)printByteCode();

        writeByteCodeToFile();
    }

    private void getCPValues(CPGenerator cpGenerator) {
        constantPool = cpGenerator.getConstantPool();
        this_class = cpGenerator.getClassIndex();
        super_class = cpGenerator.getSuperclassIndex();
        sourcefile = cpGenerator.getSourcefileIndex();
        fields = cpGenerator.getFields();
        methods = cpGenerator.getMethods();
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
        insertShort(this_class);
        //u2 - super_class;
        insertShort(super_class);

        //u2 - interfaces count
        insertShort(countInterfaces);

        insertShort((short)fields.size());
        insertFields();

        insertShort((short)methods.size());
        insertMethods();

        //ATTRIBUTES
        insertAttributes();

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

    /*  https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.5
        u2             access_flags;
        u2             name_index;
        u2             descriptor_index;
        u2             attributes_count;
        attribute_info attributes[attributes_count];
    */
    private void insertFields() {
        for (Field f : fields) {
            insertShort(f.getAccessFlag());
            insertShort(f.getNameIndex());
            insertShort(f.getSignatureIndex());
            insertShort(f.getCountAttributes());
            if(f.getAttributes() != null){
                for (Attribut a : f.getAttributes()) {
                    insertShort(a.getNameIndex());
                    insertInt(a.getLength());
                    insertShort(a.getIndex());
                }
            }
        }
    }

     /* https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.6
        u2             access_flags;
        u2             name_index;
        u2             descriptor_index;
        u2             attributes_count;
        attribute_info attributes[attributes_count];
    */
    private void insertMethods() {
        for (Method m : methods) {
            insertShort(m.getAccessFlag());
            insertShort(m.getNameIndex());
            insertShort(m.getSignatureIndex());
            insertShort(m.getCountAttributes());


            if(m.getAttributes() != null ){
                /* https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.7.3
                    u2 attribute_name_index;
                    u4 attribute_length;
                    u2 max_stack;
                    u2 max_locals;
                    u4 code_length;
                    u1 code[code_length];
                    u2 exception_table_length;
                    u2 attributes_count;
                    attribute_info attributes[attributes_count];
                */
                for (Attribut codeAtt : m.getAttributes()) {
                    insertShort(codeAtt.getNameIndex());
                    insertInt(codeAtt.getLength());
                    insertShort(codeAtt.getStackSize());
                    insertShort(codeAtt.getCountLocalVars());
                    insertInt(codeAtt.getCodeLength());
                    insertByteArray(codeAtt.getCode());
                    insertShort(codeAtt.getCountExceptionTable());
                    insertShort(codeAtt.getCountAttributes());

                    if(codeAtt.getAttributes() != null ){
                        /* https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.7.12
                            u2 attribute_name_index;
                            u4 attribute_length;
                            u2 line_number_table_length;
                            {   u2 start_pc;
                                u2 line_number;
                            } line_number_table[line_number_table_length];
                        */
                        for (Attribut attribut: codeAtt.getAttributes()) {
                            insertShort(attribut.getNameIndex());
                            insertInt(attribut.getLength());
                            insertShort(attribut.getCountAttributes());

                            if(attribut.getAttributes() != null ) {
                                /* https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.7.12
                                    u2 start_pc;
                                    u2 line_number;
                                */
                                for (Attribut lnt : attribut.getAttributes()) {
                                    insertShort(lnt.getStart_pc());
                                    insertShort(lnt.getLine_number());
                                }
                            }
                        }
                    }

                }
            }
        }
    }


    /*  https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.7
        u2 attributes_count
        u2 attribute_name_index
        u4 attribute_length
        u2 sourcefile
    */
    private void insertAttributes() {               //TODO
        insertShort((short) 1);
        insertShort(sourcefile);
        insertInt(2);
        insertShort((short)(sourcefile + 1));
    }


    //-----------------------------------------------------------------------------------------------------------------

    void insertInt(int cp) {
        buffer.putInt(cp);
    }

    void insertShort(short cp) {
        buffer.putShort(cp);
    }

    void insertByte(byte cp) {
        buffer.put(cp);
    }

    void insertByteArray(byte[] cp) {
        buffer.put(cp);
    }

    void insertString(String s) {
        buffer.put(s.getBytes());
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
    }

}
