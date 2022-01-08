package AST;

import Data.STObject;
import Data.SymbolTable;
import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class ASTTests {

    // files
    private static final String FSU_TEST                = "./src/test/resources/pass_test/Test.java";

    // tests
    @Test
    public void FSUTest_complete(){ runOverFile(FSU_TEST); }

    private void runOverFile(String path){
        try {
            Parser parser = new Parser(path);
            parser.parseFile();


            AST ast = parser.getAst();
            List<ASTNodeContainer> nodeContainers = ast.getNodeContainers();

            System.out.println("");
            System.out.println("------------------------------------------AST------------------------------------------");
            System.out.println("[" + ast.ID + " : " + ast.getObject().getName()+ "]");

            //Finals
            ASTNodeContainer finals = nodeContainers.get(0);
            System.out.println("  [" + finals.getID() + " : " + finals.getcName() + "]");

            List<ASTNode> finalNodes = finals.getNodes();
            if(!finalNodes.isEmpty()){
                for (ASTNode node : finalNodes) {
                    System.out.println("    (" + node.getId() + " : " + node.getObject().getName() + ")");
                    System.out.println("      (" + node.getLeft().getId() + " : " + node.getLeft().getConstant() + ")");
                }
            }

            //Vars
            ASTNodeContainer vars = nodeContainers.get(1);
            System.out.println("  [" + vars.getID() + " : " + vars.getcName() + "]");

            List<ASTNode> varnodes = vars.getNodes();
            if(!varnodes.isEmpty()){
                for (ASTNode node : varnodes) {
                    System.out.println("    (" + node.getId() + " : " + node.getObject().getName() + ")");
                }
            }

            //Methods
            ASTNodeContainer methods = nodeContainers.get(2);
            System.out.println("  [" + methods.getID() + " : " + methods.getcName() + "]");

            List<ASTNode> methodnode = methods.getNodes();
            if(!methodnode.isEmpty()){
                for (ASTNode node : methodnode) {
                    System.out.println("    (" + node.getId() + " : " + node.getObject().getName() + ")");
                }
            }


            System.out.println("---------------------------------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
