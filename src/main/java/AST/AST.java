package AST;

import Data.STObject;

import java.util.List;

public class AST {

    int ID;
    STObject object;

    List<ASTNodeContainer> nodeContainers;

    public AST(int ID, STObject object) {
        this.ID = ID;
        this.object = object;
    }

    public void setNodeContainers(List<ASTNodeContainer> nodeContainers) {
        this.nodeContainers = nodeContainers;
    }

    public int getID() {
        return ID;
    }

    public STObject getObject() {
        return object;
    }

    public List<ASTNodeContainer> getNodeContainers() {
        return nodeContainers;
    }
}
