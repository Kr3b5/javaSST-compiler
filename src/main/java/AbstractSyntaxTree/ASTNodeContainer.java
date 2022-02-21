package AbstractSyntaxTree;

import java.util.List;

public class ASTNodeContainer {

    private int ID;
    private String cName;
    private List<ASTNode> nodes;

    public ASTNodeContainer(int ID, String cName) {
        this.ID = ID;
        this.cName = cName;
    }

    public void setNodes(List<ASTNode> nodes) {
        this.nodes = nodes;
    }

    public int getID() {
        return ID;
    }

    public String getcName() {
        return cName;
    }

    public List<ASTNode> getNodes() {
        return nodes;
    }
}
