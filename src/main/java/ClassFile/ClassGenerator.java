package ClassFile;

import AbstractSyntaxTree.AST;
import AbstractSyntaxTree.ASTClass;
import AbstractSyntaxTree.ASTNode;
import AbstractSyntaxTree.ASTNodeContainer;
import ClassData.*;
import Data.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * Generator for class code
 * https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html#jvms-4.4
 *
 * @author Kr3b5
 */
public class ClassGenerator {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(ClassGenerator.class.getName());


    private short countConstantPool;
    private final HashMap<Short, CPConstant> constantPool;
    private final LinkedList<Field> fields;
    private final LinkedList<Method> methods;

    private final AST ast;

    private final List<String> called;

    private short classIndex;
    private short superclassIndex;
    private short sourcefileIndex;
    private short codeIndex;

    private final ByteBuffer codeBuffer = ByteBuffer.allocate(65536);
    private int cur;

    private List<StackSafe> stackSafes;
    private List<Short> field_ref;
    private boolean typeInt;

    private int countMethods;
    private boolean containsReturnNode;
    private boolean containsLastReturnVoid;

    private short maxStackSize;
    private short stackSize;

    //debug
    boolean debugMode;

    public ClassGenerator(AST ast) {
        this.countConstantPool = 1;
        this.constantPool = new HashMap<>();
        this.fields = new LinkedList<>();
        this.methods = new LinkedList<>();
        this.ast = ast;
        this.called = new LinkedList<>();
        debugMode = false;
        typeInt = false;
    }

    //SETTER + GETTER
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public LinkedList<Field> getFields() { return fields; }
    public LinkedList<Method> getMethods() { return methods; }
    public short getClassIndex() { return classIndex; }
    public short getSuperclassIndex() { return superclassIndex; }
    public short getSourcefileIndex() { return sourcefileIndex; }
    public HashMap<Short, CPConstant> getConstantPool() { return constantPool;}


    /**
     * Generate Constant Pool + Code
     */
    public void generate(){
        genConstantPool();
        genCode();
    }


    /**
     * Generate Constant Pool
     */
    public void genConstantPool() {
        genPoolHead();
        genPoolClass();
        genPoolFinals();
        genPoolCalls();
        genPoolConstants();
        genNotCalledVars();
        genPoolCodeHead();
        genPoolCodeBody();
        genPoolEnd();

        if(debugMode) printConstantPool();
    }

    /*
     #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
      #2 = Class              #4             // java/lang/Object
      #3 = NameAndType        #5:#6          // "<init>":()V
      #4 = Utf8               java/lang/Object
      #5 = Utf8               <init>
      #6 = Utf8               ()V
    */
    /**
     * generate Constantpool part - head
     */
    private void genPoolHead() {
        addToPool(new CPConstant((byte) CPTypes.METHOD.value, (short) 2, (short) 3));
        superclassIndex = countConstantPool;
        addToPool(new CPConstant((byte) CPTypes.CLASS.value, (short) 4));
        addToPool(new CPConstant((byte) CPTypes.NAMEANDTYPE.value, (short) 5, (short) 6));
        String obj = "java/lang/Object";
        addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) obj.length(), obj));
        String init = "<init>";
        addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) init.length(), init));
        String v = "()V";
        addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) v.length(), v));
    }

    /*
      #7 = Class              #8             // EmptyClass
      #8 = Utf8               EmptyClass
    */
    /**
     * generate Constantpool part - class
     */
    private void genPoolClass() {
        classIndex = countConstantPool;
        addToPool(new CPConstant((byte) CPTypes.CLASS.value, (short) (countConstantPool + 1)));
        methods.add(new Method((short)1, (short) 5, (short) 6, (short) 0, null));
        addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) ast.getObject().getName().length(), ast.getObject().getName()));
    }

    /* FINALS
        #7 = Fieldref           #8.#9          // ClassTest2.fvar1:I
        #9 = NameAndType        #11:#12        // fvar1:I
        #11 = Utf8               fvar1
        #12 = Utf8               I
     */
    /**
     * generate Constantpool part - finals
     */
    private void genPoolFinals() {
        field_ref = new LinkedList<>();

        ASTNodeContainer finals = ast.getFinals();
        for (ASTNode node : finals.getNodes()) {
            short nameIndex;
            STObject stobject = node.getObject();
            field_ref.add(countConstantPool);
            addToPool(new CPConstant((byte) CPTypes.FIELD.value, classIndex , (short) (countConstantPool + 1)));
            short key = getKeyByStringValue("I");
            if( key != 0){
                addToPool(new CPConstant((byte) CPTypes.NAMEANDTYPE.value, (short) (countConstantPool + 1), key));
                nameIndex = countConstantPool;
                addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) stobject.getName().length(), stobject.getName()));
            }else{
                addToPool(new CPConstant((byte) CPTypes.NAMEANDTYPE.value, (short) (countConstantPool + 1), (short) (countConstantPool + 2)));
                nameIndex = countConstantPool;
                addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) stobject.getName().length(), stobject.getName()));
                key = countConstantPool;
                addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short)1, "I"));
            }

            // add Fields to Fieldlist
            // ACC_FINAL 0x0010
            Field field = new Field((short)0x10, nameIndex, key, (short) 0, null);
            fields.add(field);
        }
    }

    /* METHOD Called!
        #16 = Methodref          #8.#17         // ClassTest2.meth2:(II)I
        #17 = NameAndType        #18:#19        // meth2:(II)I
        #18 = Utf8               meth2
        #19 = Utf8               (II)I
     */
    /* Globals
        #20 = Fieldref           #8.#21         // ClassTest2.dyn1:I
        #21 = NameAndType        #22:#12        // dyn1:I
        #22 = Utf8               dyn1
     */
    /**
     * generate Constantpool part - called VAR + METHODS
     */
    private void genPoolCalls() {
        ASTNodeContainer methodsList = ast.getMethods();
        for (ASTNode n : methodsList.getNodes()) {
            findNextCalledNode(n.getLink());
        }

        for (ASTNode n : methodsList.getNodes()) {
            if(called.contains(n.getObject().getName())){
                String pKey = getPKey(n);
                short key = getKeyByStringValue(pKey);
                if( key != 0){
                    addToPool(new CPConstant((byte) CPTypes.METHOD.value, classIndex , (short) (countConstantPool + 1)));
                    addToPool(new CPConstant((byte) CPTypes.NAMEANDTYPE.value, (short) (countConstantPool + 1), key));
                    addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) n.getObject().getName().length(), n.getObject().getName()));
                }else{
                    addToPool(new CPConstant((byte) CPTypes.METHOD.value, classIndex , (short) (countConstantPool + 1)));
                    addToPool(new CPConstant((byte) CPTypes.NAMEANDTYPE.value, (short) (countConstantPool + 1), (short) (countConstantPool + 2)));
                    addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) n.getObject().getName().length(), n.getObject().getName()));
                    addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) pKey.length(), pKey));
                }
            }
        }

        ASTNodeContainer globals = ast.getVars();
        for (ASTNode n : globals.getNodes()) {
            if(called.contains(n.getObject().getName())){
                short key = getKeyByStringValue("I");
                short nameIndex = (short) (countConstantPool + 2);
                if( key != 0){
                    addToPool(new CPConstant((byte) CPTypes.FIELD.value, classIndex , (short) (countConstantPool + 1)));
                    addToPool(new CPConstant((byte) CPTypes.NAMEANDTYPE.value, (short) (countConstantPool + 1), key));
                    addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) n.getObject().getName().length(), n.getObject().getName()));
                }else{
                    addToPool(new CPConstant((byte) CPTypes.FIELD.value, classIndex , (short) (countConstantPool + 1)));
                    addToPool(new CPConstant((byte) CPTypes.NAMEANDTYPE.value, (short) (countConstantPool + 1), (short) (countConstantPool + 2)));
                    addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) n.getObject().getName().length(), n.getObject().getName()));
                    key = countConstantPool;
                    addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short)1, "I"));
                }
                fields.add(new Field((short)0, nameIndex, key, (short) 0, null));
            }
        }
    }


    /**
     * Find called VAR + PROD
     */
    private void findNextCalledNode(ASTNode node){
        if(node.getNodeClass().equals(ASTClass.VAR) ||
                node.getNodeClass().equals(ASTClass.PROD)){
            if(!called.contains(node.getName())){
                called.add(node.getName());
            }
            if(node.getObject() != null){
                findNextCalledSubNodes(node.getObject().getSymtab());
            }
        }
        if(node.getLeft() != null){
            findNextCalledNode(node.getLeft());
        }
        if(node.getRight() != null){
            findNextCalledNode(node.getRight());
        }
        if(node.getLink() != null){
            findNextCalledNode(node.getLink());
        }
    }

    /**
     * Find called subnodes (Symboltable)
     */
    private void findNextCalledSubNodes(SymbolTable st){
        for (STObject stObject: st.getObjects()) {
            if(stObject.getObjClass().equals(ObjClass.PROC)){
                if(!called.contains(stObject.getName())){
                    called.add(stObject.getName());
                }
                if(stObject.getSymtab() != null) findNextCalledSubNodes(stObject.getSymtab());
            }
        }
    }


    /*
         #30 = Utf8               ConstantValue
         #31 = Integer            1
     */
    /**
     * generate Constantpool part - Constant values
     */
    private void genPoolConstants(){
        ASTNodeContainer finals = ast.getFinals();
        if( !finals.getNodes().isEmpty()){
            String c = "ConstantValue";
            short constantValueIndex = countConstantPool;
            int i = 0;
            addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) c.length(), c));
            for (ASTNode node : finals.getNodes()) {
                STObject stobject = node.getObject();
                Attribut constantValue = new Attribut(constantValueIndex, 2, countConstantPool);
                addToPool(new CPConstant((byte) CPTypes.INTEGER.value, stobject.getIntValue()));

                fields.get(i).setCountAttributes((short)1);
                List<Attribut> attributes = new LinkedList<>();
                attributes.add(constantValue);
                fields.get(i).setAttributes(attributes);
                i++;
            }
        }
    }

    /*
         #40 = Utf8               var1
    */
    /**
     * generate Constantpool part - not called VAR
     */
    private void genNotCalledVars(){
        ASTNodeContainer globals = ast.getVars();
        for (ASTNode n : globals.getNodes()) {
            if(!called.contains(n.getObject().getName())){
                // add Globals to Fieldlist
                Field field = new Field((short)0, countConstantPool, getKeyByStringValue("I"), (short) 0, null);
                fields.add(field);

                addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) n.getObject().getName().length(), n.getObject().getName()));
            }
        }
    }


    /*
       #9 = Utf8               Code
      #10 = Utf8               LineNumberTable
     */
    /**
     * generate Constantpool part - Code head
     */
    private void genPoolCodeHead() {
        String c = "Code";
        codeIndex = countConstantPool;
        addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) c.length(), c));
        String lnt = "LineNumberTable";
        addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) lnt.length(), lnt));
    }

    /*
     #40 = Utf8               meth1
     #41 = Utf8               (I)V
    */
    /**
     * generate Constantpool part - not called methods
     */
    private void genPoolCodeBody() {
        ASTNodeContainer methodList = ast.getMethods();
        for (ASTNode n : methodList.getNodes()) {
            if(!called.contains(n.getObject().getName())){
                String pKey = getPKey(n);
                short key = getKeyByStringValue(pKey);
                addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) n.getObject().getName().length(), n.getObject().getName()));
                if (key == 0) {
                    addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) pKey.length(), pKey));
                }
            }
        }
    }

    /*
      #11 = Utf8               SourceFile
      #12 = Utf8               EmptyClass.java
     */
    /**
     * generate Constantpool part - end
     */
    private void genPoolEnd() {
        String sf = "SourceFile";
        String name = ast.getObject().getName() + ".java";
        sourcefileIndex = countConstantPool;
        addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) sf.length(), sf));
        addToPool(new CPConstant((byte) CPTypes.UTF8.value, (short) name.length(), name));
    }


    // HELPER
    /**
     * Add to Constant Pool
     * @param c Constantpoolconstant
     */
    private void addToPool(CPConstant c) {
        constantPool.put(countConstantPool, c);
        countConstantPool++;
    }

    /**
     * Find parameterkey from method
     * @param value name
     * @return parameterkey
     */
    private Short getKeyByStringValue(String value) {
        for (Map.Entry<Short,CPConstant> entry : constantPool.entrySet()) {
            if (entry.getValue().getsValue() != null && value.equals(entry.getValue().getsValue())) {
                return entry.getKey();
            }
        }
        return 0;
    }

    /**
     * generate parameterkey from method
     * @param n methodnode
     * @return paramterkey
     */
    private String getPKey(ASTNode n){
        int cInts = 0;
        for (STObject obj : n.getObject().getSymtab().getObjects()) {
            if (obj.getObjClass().equals(ObjClass.PAR)) {
                cInts++;
            }
        }
        //parameter key
        StringBuilder sb = new StringBuilder("(");
        sb.append("I".repeat(Math.max(0, cInts)));
        sb.append(")");
        if(n.getObject().getSTType().equals(STType.INT)){
            sb.append("I");
        }else{
            sb.append("V");
        }
        return sb.toString();
    }

    // DEBUG
    /**
     * Debug - print Constantpool
     */
    private void printConstantPool(){
        logger.info("Constant Pool");
        int count = 1;
        for (CPConstant c: constantPool.values()) {
            byte type = c.getType();
            String prestring = "   #" + count;
            if(constantPool.size() >= 10 && count < 10){
                prestring = "    #" + count;
            }
            if((byte) CPTypes.CLASS.value == type){
                logger.info(prestring +
                            " = Class \t\t\t#" + c.getBytefield1());
            }else if((byte) CPTypes.UTF8.value == type){
                logger.info(prestring +
                            " = UTF8 \t\t\t" + c.getsValue());
            }else if((byte) CPTypes.FIELD.value == type){
                logger.info(prestring +
                             " = Fieldref \t\t#" + c.getBytefield1() + ".#" + c.getBytefield2());
            }else if((byte) CPTypes.INTEGER.value == type){
                logger.info(prestring +
                            " = Integer \t\t\t" + c.getiValue());
            }else if((byte) CPTypes.METHOD.value == type){
                logger.info(prestring +
                            " = Methodref \t\t#" + c.getBytefield1() + ".#" + c.getBytefield2());
            }else if((byte) CPTypes.NAMEANDTYPE.value == type){
                logger.info(prestring +
                            " = NameAndType \t\t#" + c.getBytefield1() + ":#" + c.getBytefield2());
            }else{
                logger.error("Unknown Type");
            }
            count++;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // CODEGEN

    /**
     * generate method code
     */
    public void genCode(){
        addMethods();
        genClassCode();
        genMethodCode();
    }

    /**
     * Add methods to list
     */
    private void addMethods() {
        ASTNodeContainer methodsList = ast.getMethods();
        for (ASTNode n : methodsList.getNodes()) {
            String pKey = getPKey(n);
            short signatureIndex    = getKeyByStringValue(pKey);
            short nameIndex         = getKeyByStringValue(n.getObject().getName());
            methods.add(new Method((short)1, nameIndex, signatureIndex, (short) 0, null));
        }
    }

    /**
     * generate classcode
     */
    private void genClassCode(){
        codeBuffer.clear();
        cur = 0;

        List<Attribut> attCode = new LinkedList<>();

        //GENHEAD
        insertByte(InsSet.ALOAD_0.bytes);
        insertByte(InsSet.INVOKESPECIAL.bytes);
        insertShort((short)1);

        //GENCODE
        int i = 0;
        ASTNodeContainer finals = ast.getFinals();
        for (ASTNode node : finals.getNodes()) {
            STObject stobject = node.getObject();

            insertByte(InsSet.ALOAD_0.bytes);

            byte cons = getConst(stobject.getIntValue());
            insertByte(cons);
            if( cons == InsSet.BIPUSH.bytes ){
                insertByte((byte)stobject.getIntValue());
            }

            insertByte(InsSet.PUTFIELD.bytes);
            insertShort(field_ref.get(i));
            i++;
        }

        //GENEND
        insertByte(InsSet.RETURN.bytes);

        byte[] code = new byte[cur];
        codeBuffer.get(0, code, 0, code.length);


        short size = (short)(12 + cur);
        Attribut classCode = new Attribut(codeIndex, size, (short)2, (short)1, cur, code, (short)0, null);
        attCode.add(classCode);

        methods.get(0).setCountAttributes((short)1);
        methods.get(0).setAttributes(attCode);
    }

    /**
     * generate methodcode
     */
    private void genMethodCode(){
        for (ASTNode methodroot : ast.getMethods().getNodes()) {
            stackSafes = new LinkedList<>();
            codeBuffer.clear();
            cur = 0;
            stackSize = maxStackSize = 0;
            short locals = (short) (methodroot.getObject().getSymtab().getObjects().size() + 1);

            //getParameter
            int i = 1;
            for (STObject param : methodroot.getObject().getSymtab().getObjects()) {
                if(param.getObjClass().equals(ObjClass.PAR)) stackSafes.add(new StackSafe(i, param.getName())); i++;
            }

            //get Type for Return
            typeInt = methodroot.getObject().getSTType().equals(STType.INT);

            //analyze all Subnodes
            analyzeNextNode(methodroot.getLink());

            if(!typeInt && !containsLastReturnVoid) setReturn();

            byte[] code = new byte[cur];
            codeBuffer.get(0, code, 0, code.length);

            short size = (short)(12 + cur);
            Attribut classCode = new Attribut(codeIndex, size, maxStackSize, locals, cur, code, (short)0, null);

            List<Attribut> attCode = new LinkedList<>();
            attCode.add(classCode);

            int mID = getMethodsIndex(methodroot.getObject().getName());
            methods.get(mID).setCountAttributes((short)1);
            methods.get(mID).setAttributes(attCode);
        }
    }

    /**
     * find methodindex in list methods
     * @param methodName Name of method
     * @return index
     */
    private int getMethodsIndex(String methodName){
        int index = 0;
        for (Method m: methods) {
            if(getCPNamebyIndex(m.getNameIndex()).equals(methodName)) {
                return index;
            }
            index++;
        }
        return 0;
    }

    /**
     * Find UTF-8 entry by index
     * @param index CP index
     * @return string value
     */
    private String getCPNamebyIndex(short index){
        return constantPool.get(index).getsValue();
    }

    /**
     * Generate code from node
     * @param n node
     */
    private void analyzeNextNode(ASTNode n) {
        containsLastReturnVoid = false;
        if(n.getNodeClass().equals(ASTClass.ASSIGN)){
            if(assignGlobal(n.getLeft().getName())) insertByte(InsSet.ALOAD_0.bytes);
            analyzeNextNode(n.getRight());
            setVar(n.getLeft().getName());
        }
        else if(n.getNodeClass().equals(ASTClass.VAR)){
            loadVar(n.getName());
        }
        else if(n.getNodeClass().equals(ASTClass.INT)){
            setInt(n.getConstant());
        }
        else if(n.getNodeClass().equals(ASTClass.PROD)){
            callProd(n);
        }
        else if(n.getNodeClass().equals(ASTClass.BINOP)){
            analyzeNextNode(n.getLeft());
            analyzeNextNode(n.getRight());
            setOperator(n);
        }
        else if(n.getNodeClass().equals(ASTClass.IF_ELSE)){
            setIfElse(n);
        }
        else if(n.getNodeClass().equals(ASTClass.WHILE)){
            setWhile(n);
        }
        else if(n.getNodeClass().equals(ASTClass.RETURN)){
            if(n.getLeft() != null) analyzeNextNode(n.getLeft());
            setReturn();
            containsReturnNode = containsLastReturnVoid = true;
        }

        if(n.getLink() != null) analyzeNextNode(n.getLink());
    }

    /**
     * Generate while
     * @param n node
     */
    private void setWhile(ASTNode n) {
        int posBegin = cur;
        int posEnd;

        analyzeNextNode(n.getLeft()); //BINOP without Location

        //set pos-End temp to short 0
        posEnd = cur;
        insertShort((short)0);

        analyzeNextNode(n.getRight());

        //set GOTO
        insertByte(InsSet.GOTO.bytes);
        insertShort((short)(posBegin - (cur-1)));

        //replace pos End
        replaceShort(posEnd, (short)(cur - (posEnd-1)));
    }

    /**
     * Generate IF-ELSE
     * @param n node
     */
    private void setIfElse(ASTNode n) {
        int posElse;
        int posEnd = 0;
        containsReturnNode = false;

        // IF
        ASTNode ifNode = n.getLeft();
        analyzeNextNode(ifNode.getLeft());      //BINOP without Location

        //set pos-Else short temp to 0
        posElse = cur;
        insertShort((short)0);

        analyzeNextNode(ifNode.getRight());

        //set GOTO
        if(!containsReturnNode){
            insertByte(InsSet.GOTO.bytes);
            //set pos-End short temp to 0
            posEnd = cur;
            insertShort((short)0);
        }

        //ELSE
        //replace pos-Else
        replaceShort(posElse, (short)(cur - (posElse-1)));

        analyzeNextNode(n.getRight());

        //replace GOTO
        if(!containsReturnNode) replaceShort(posEnd, (short)(cur - (posEnd-1)));
    }

    /**
     * set return code
     */
    private void setReturn() {
        if(typeInt){
            insertByte(InsSet.IRETURN.bytes);
        }else{
            insertByte(InsSet.RETURN.bytes);
        }
    }

    /**
     * set BINOP code
     * @param n node
     */
    private void setOperator(ASTNode n) {
        decreaseStack();
        if(n.getNodeSubclass().equals(TokenType.PLUS)) {                // +
            insertByte(InsSet.IADD.bytes);
        }
        else if(n.getNodeSubclass().equals(TokenType.MINUS)) {          // -
            insertByte(InsSet.ISUB.bytes);
        }
        else if(n.getNodeSubclass().equals(TokenType.TIMES)) {          // *
            insertByte(InsSet.IMUL.bytes);
        }
        else if(n.getNodeSubclass().equals(TokenType.SLASH)) {          // /
            insertByte(InsSet.IDIV.bytes);
        }
        else if(n.getNodeSubclass().equals(TokenType.EQUAL)) {          // ==  -> !=
            insertByte(InsSet.IFICMPNE.bytes);
            decreaseStack();
        }
        else if(n.getNodeSubclass().equals(TokenType.NEQUAL)) {         // !=  -> ==
            insertByte(InsSet.IFICMPEQ.bytes);
            decreaseStack();
        }
        else if(n.getNodeSubclass().equals(TokenType.GREATER)) {        // >  -> <=
            insertByte(InsSet.IFICMPLE.bytes);
            decreaseStack();
        }
        else if(n.getNodeSubclass().equals(TokenType.GR_EQ)) {          // >= -> <
            insertByte(InsSet.IFICMPLT.bytes);
            decreaseStack();
        }
        else if(n.getNodeSubclass().equals(TokenType.SMALLER)) {        // <  -> >=
            insertByte(InsSet.IFICMPGE.bytes);
            decreaseStack();
        }
        else if(n.getNodeSubclass().equals(TokenType.SM_EQ)) {          // <= -> >
            insertByte(InsSet.IFICMPGT.bytes);
            decreaseStack();
        }
    }

    /**
     * set INT
     * @param z value
     */
    private void setInt(int z){
        byte cons = getConst(z);
        insertByte(cons);
        if( cons == InsSet.BIPUSH.bytes ){
            insertByte((byte)z);
        }
        increaseStack();
    }

    /**
     * set VAR - save
     * @param var name
     */
    private void setVar(String var){
        STObject stObjectGlobal = isGlobal(var);
        if(stObjectGlobal != null){
            insertByte(InsSet.PUTFIELD.bytes);
            insertShort(getRef(stObjectGlobal.getName()));
        }else{
            int id = getStackID(var);
            if(id != 0){
                insertByte(getIStore(id));
                if(id > 3){
                    insertByte((byte) id);
                }
            }else{
                id = stackSafes.size()+1;
                insertByte(getIStore(id));
                if(id > 3){
                    insertByte((byte) id);
                }
                stackSafes.add(new StackSafe(id, var));
            }
        }
        decreaseStack();
    }

    /**
     * load VAR
     * @param var name
     */
    private void loadVar(String var){
        STObject stObjectFinal = isFinal(var);
        STObject stObjectGlobal = isGlobal(var);
        if(stObjectFinal != null){                      // Var is final
            setInt(stObjectFinal.getIntValue());
        }else if(stObjectGlobal != null){               // Var is global
            insertByte(InsSet.ALOAD_0.bytes);
            insertByte(InsSet.GETFIELD.bytes);
            insertShort(getRef(stObjectGlobal.getName()));
        }else{                                          // Var is local
            byte b = getILoad(getStackID(var));
            insertByte(b);
            if(b == InsSet.ILOAD.bytes) insertByte((byte) getStackID(var));
        }
        increaseStack();
    }

    /**
     * test if VAR is final
     * @param varName name
     * @return Symboltableobject
     */
    private STObject isFinal(String varName){
        return findFGNode(varName, ast.getFinals().getNodes());
    }

    /**
     * test if VAR is global
     * @param varName name
     * @return Symboltableobject
     */
    private STObject isGlobal(String varName){
        return findFGNode(varName, ast.getVars().getNodes());
    }

    /**
     * test if VAR is global
     * @param varName name
     * @return true/false
     */
    private boolean assignGlobal(String varName){
        return findFGNode(varName, ast.getVars().getNodes()) != null;
    }

    /**
     * Find Node
     * @param varName name
     * @param nodes list of nodes
     * @return Symboltableobject
     */
    private STObject findFGNode(String varName, List<ASTNode> nodes){
        for (ASTNode n : nodes) {
            if(varName.equals(n.getObject().getName())){
                return n.getObject();
            }
        }
        return null;
    }

    /**
     * get id from VAR Safe
     * @param var name
     * @return index
     */
    private int getStackID(String var){
        for (StackSafe s : stackSafes) {
            if(s.getVar().equals(var)){
                return s.getIndex();
            }
        }
        return 0;
    }

    /*
     9: aload_0
    10: iload_2
    11: iload_3
    12: invokevirtual #25                 // Method meth2:(II)I
     */
    /**
     * generate PROD code
     * @param n node
     */
    private void callProd(ASTNode n){
        //ALOAD
        int countMeth = getCountMethods(n.getObject().getSymtab());
        for (int i = 0; i < countMeth; i++){
            insertByte(InsSet.ALOAD_0.bytes);
        }

        stToByteCode(n.getObject().getSymtab());

        insertByte(InsSet.INVOKEVIRTUAL.bytes);
        insertShort(getRef(n.getName()));
    }

    /**
     * generate parameter from PROD call
     * @param st symboltable
     */
    private void stToByteCode(SymbolTable st){
        for (STObject stObject: st.getObjects()) {
            if(stObject.getObjClass().equals(ObjClass.PROC)){
                stToByteCode(stObject.getSymtab());
                insertByte(InsSet.INVOKEVIRTUAL.bytes);
                insertShort(getRef(stObject.getName()));
            }else if(stObject.getObjClass().equals(ObjClass.CONST)){
                setInt(Integer.parseInt(stObject.getName()));
            }else{
                loadVar(stObject.getName());
            }
        }
    }

    /**
     * get reference from UTF-8
     * @param varname name
     * @return Index
     */
    private short getRef(String varname){
        return getRefKeyByNameandType(getKeyByStringValue(varname));
    }

    /**
     * get reference from NameAndType
     * @param index NameAndType index
     * @return Index
     */
    private Short getRefKeyByNameandType(short index) {
        short refindex = 0;
        for (Map.Entry<Short,CPConstant> entry : constantPool.entrySet()) {
            if (index == entry.getValue().getBytefield1()) {
                refindex = entry.getKey();
            }
        }
        for (Map.Entry<Short,CPConstant> entry : constantPool.entrySet()) {
            if (refindex == entry.getValue().getBytefield2()) {
                refindex = entry.getKey();
            }
        }
        return refindex;
    }

    /**
     * get method count
     * @param st symboltable
     * @return count
     */
    private int getCountMethods(SymbolTable st){
        countMethods = 1;
        getObjects(st);
        return countMethods;
    }

    /**
     * iterate over objects and count methods
     * @param st symboltable
     */
    private void getObjects(SymbolTable st){
        for (STObject stObject: st.getObjects()) {
            if(stObject.getObjClass().equals(ObjClass.PROC)){
                countMethods++;
                getObjects(stObject.getSymtab());
            }
        }
    }

    /**
     * get instruction CONST
     * @param z value
     * @return bytecode
     */
    private byte getConst(int z){
        return switch (z) {
            case 0 -> InsSet.ICONST_0.bytes;
            case 1 -> InsSet.ICONST_1.bytes;
            case 2 -> InsSet.ICONST_2.bytes;
            case 3 -> InsSet.ICONST_3.bytes;
            case 4 -> InsSet.ICONST_4.bytes;
            case 5 -> InsSet.ICONST_5.bytes;
            default -> InsSet.BIPUSH.bytes;
        };
    }

    /**
     * get instruction STORE
     * @param z value
     * @return bytecode
     */
    private byte getIStore(int z){
        return switch (z) {
            case 0 -> InsSet.ISTORE_0.bytes;
            case 1 -> InsSet.ISTORE_1.bytes;
            case 2 -> InsSet.ISTORE_2.bytes;
            case 3 -> InsSet.ISTORE_3.bytes;
            default -> InsSet.ISTORE.bytes;
        };
    }

    /**
     * get instruction LOAD
     * @param id id
     * @return bytecode
     */
    private byte getILoad(int id){
        return switch (id) {
            case 0 -> InsSet.ILOAD_0.bytes;
            case 1 -> InsSet.ILOAD_1.bytes;
            case 2 -> InsSet.ILOAD_2.bytes;
            case 3 -> InsSet.ILOAD_3.bytes;
            default -> InsSet.ILOAD.bytes;
        };
    }

    /**
     * insert short into code
     * @param cp codepart
     */
    private void insertShort(short cp) {
        codeBuffer.putShort(cp);
        cur = cur + 2;
    }

    /**
     * replace short in code
     * @param index index
     * @param cp codepart
     */
    private void replaceShort(int index, short cp) {
        codeBuffer.putShort(index, cp);
    }

    /**
     * insert byte into code
     * @param cp codepart
     */
    private void insertByte(byte cp) {
        codeBuffer.put(cp);
        cur++;
    }

    /**
     * increase stack count and test max_stack_size
     */
    private void increaseStack(){
        stackSize++;
        if(stackSize > maxStackSize) maxStackSize = stackSize;
    }

    /**
     * decrease stack count
     */
    private void decreaseStack(){
        stackSize--;
    }


}
