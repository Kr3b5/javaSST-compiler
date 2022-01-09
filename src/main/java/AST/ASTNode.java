package AST;

import Data.STObject;
import Data.TokenType;

public class ASTNode {

    private int id;
    private String name;

    private ASTNode left, right;
    private ASTNode link;

    private ASTClass nodeClass;
    private TokenType nodeSubclass;

    private STObject object;

    private int constant;


    // FinalNodes
    public ASTNode(int id, ASTNode left, STObject object) {
        this.id = id;
        this.left = left;
        this.object = object;
    }

    //INTS
    public ASTNode(int id, int constant) {
        this.id = id;
        this.nodeClass = ASTClass.INT;
        this.constant = constant;
    }

    //VarNodes
    public ASTNode(int id, STObject object) {
        this.id = id;
        this.object = object;
    }

    //Vars
    public ASTNode(int id, String name, ASTClass nodeClass) {
        this.id = id;
        this.name = name;
        this.nodeClass = nodeClass;
    }

    // Term
    public ASTNode(int id, ASTNode left, ASTClass nodeClass, TokenType nodeSubclass) {
        this.id = id;
        this.left = left;
        this.nodeClass = nodeClass;
        this.nodeSubclass = nodeSubclass;
    }

    public ASTNode(int id, ASTClass nodeClass) {
        this.id = id;
        this.nodeClass = nodeClass;
    }

    //Return
    public ASTNode(int id, ASTClass nodeClass, ASTNode left) {
        this.id = id;
        this.left = left;
        this.nodeClass = nodeClass;
    }

    public ASTNode(int id, ASTClass nodeClass, ASTNode left, ASTNode right) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.nodeClass = nodeClass;
    }

    public int getId() {
        return id;
    }

    public ASTNode getLeft() {
        return left;
    }

    public ASTNode getRight() {
        return right;
    }

    public ASTNode getLink() {
        return link;
    }

    public STObject getObject() {
        return object;
    }

    public int getConstant() {
        return constant;
    }

    public void setLeft(ASTNode left) {
        this.left = left;
    }

    public void setRight(ASTNode right) {
        this.right = right;
    }

    public void setLink(ASTNode link) {
        this.link = link;
    }

    public ASTClass getNodeClass() {
        return nodeClass;
    }

    public TokenType getNodeSubclass() {
        return nodeSubclass;
    }

    public String getName() {
        return name;
    }
}
