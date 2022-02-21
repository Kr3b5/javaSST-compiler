package AbstractSyntaxTree;

import Data.STObject;

public class AST {

    private int ID;
    private STObject object;

    private ASTNodeContainer finals;
    private ASTNodeContainer vars;
    private ASTNodeContainer methods;

    public AST(int ID, STObject object) {
        this.ID = ID;
        this.object = object;
    }

    public void setFinals(ASTNodeContainer finals) {
        this.finals = finals;
    }

    public void setVars(ASTNodeContainer vars) {
        this.vars = vars;
    }

    public void setMethods(ASTNodeContainer methods) {
        this.methods = methods;
    }

    public int getID() {
        return ID;
    }

    public STObject getObject() {
        return object;
    }

    public ASTNodeContainer getFinals() {
        return finals;
    }

    public ASTNodeContainer getVars() {
        return vars;
    }

    public ASTNodeContainer getMethods() {
        return methods;
    }
}
