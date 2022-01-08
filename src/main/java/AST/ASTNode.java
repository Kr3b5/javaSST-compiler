package AST;

import Data.STObject;

public class ASTNode {

    private int id;

    private ASTNode left, right;
    private ASTNode link;

    private short nodeClass;
    private short nodeSubclass;

    private STObject object;

    private int constant;

    public ASTNode(int id, short nodeClass, short nodeSubclass, STObject object, int constant) {
        this.id = id;
        this.nodeClass = nodeClass;
        this.nodeSubclass = nodeSubclass;
        this.object = object;
        this.constant = constant;
    }

    // FinalNodes
    public ASTNode(int id, ASTNode left, STObject object) {
        this.id = id;
        this.left = left;
        this.object = object;
    }

    //ConstantNodes
    public ASTNode(int id, int constant) {
        this.id = id;
        this.constant = constant;
    }

    //VarNodes
    public ASTNode(int id, STObject object) {
        this.id = id;
        this.object = object;
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

    public short getNodeClass() {
        return nodeClass;
    }

    public short getNodeSubclass() {
        return nodeSubclass;
    }

    public STObject getObject() {
        return object;
    }

    public int getConstant() {
        return constant;
    }
}
