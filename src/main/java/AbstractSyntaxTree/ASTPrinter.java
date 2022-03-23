package AbstractSyntaxTree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Printer for dot files
 *
 * @author Kr3b5
 */
public class ASTPrinter {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(ASTPrinter.class.getName());

    private List<ASTPrintObj> printObjs = new LinkedList<ASTPrintObj>();
    private BufferedWriter writer;
    private String filename;

    /**
     * pritn dot file
     * @param ast AST
     */
    public void printDot(AST ast){

        extractNodestoList(ast);

        try {
            createDotFile();
            writeNodes();
            writeConnections();
            endDotFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Dot file generated!");
    }

    /**
     * create file
     * @throws IOException file creation error
     */
    private void createDotFile() throws IOException {
        writer = new BufferedWriter(new FileWriter(filename + ".dot"));
        writer.write("digraph " + filename + " {");
        writer.newLine();
    }

    /**
     * write to file
     * @throws IOException write error
     */
    private void endDotFile() throws IOException {
        writer.write("}");
        writer.close();
    }

    /**
     * write nodes
     *
     * 1685538367[label="var1"];
     *
     * @throws IOException write error
     */
    private void writeNodes() throws IOException {
        for (ASTPrintObj obj: printObjs) {
                    writer.write(obj.getID() + "[label=\"" + obj.getFirstline() );
            if(obj.getSecondline() != null){
                writer.newLine();
                writer.write(obj.getSecondline());
            }
            writer.write( "\"];");
            writer.newLine();
        }

    }

    /**
     * write connections between nodes
     *
     * @throws IOException write error
     */
    private void writeConnections() throws IOException {
        for (ASTPrintObj obj: printObjs) {

            if(obj.getConLink() != 0 ){
                writer.write( obj.getID() + " -> " + obj.getConLink());
                writer.newLine();
            }
            if(obj.getConLeft() != 0 ){
                writer.write( obj.getID() + " -> " + obj.getConLeft());
                writer.newLine();
            }
            if(obj.getConRight() != 0 ){
                writer.write( obj.getID() + " -> " + obj.getConRight());
                writer.newLine();
            }

            if(!obj.getConnections().isEmpty()){
                for (int con : obj.getConnections()) {
                    writer.write( obj.getID() + " -> " + con);
                    writer.newLine();
                }
            }
        }

    }

    /**
     * extract nodes from AST to list
     *
     * @param ast AST
     */
    private void extractNodestoList(AST ast){
        ASTNodeContainer finals = ast.getFinals();
        ASTNodeContainer vars = ast.getVars();
        ASTNodeContainer methods = ast.getMethods();

        //Starter Node
        printObjs.add(new ASTPrintObj(ast.getID(),  ast.getObject().getName(), null , finals.getID(), vars.getID(), methods.getID() ));
        filename = ast.getObject().getName();

        //Container
        ASTPrintObj cFinals = new ASTPrintObj(finals.getID(),  finals.getcName());
        ASTPrintObj cVars = new ASTPrintObj(vars.getID(),  vars.getcName());
        ASTPrintObj cMethods = new ASTPrintObj(methods.getID(),  methods.getcName());

        // Finals
        List<ASTNode> finalNodes = finals.getNodes();
        if(!finalNodes.isEmpty()){
            for (ASTNode node : finalNodes) {
                printObjs.add(new ASTPrintObj(node.getId(), node.getObject().getName(), null,  node.getLeft().getId() ));
                cFinals.setConnections(node.getId());
                printObjs.add(new ASTPrintObj(node.getLeft().getId(), node.getLeft().getNodeClass().name(), String.valueOf(node.getLeft().getConstant()) ));
            }
        }

        //Vars
        List<ASTNode> varnodes = vars.getNodes();
        if(!varnodes.isEmpty()){
            for (ASTNode node : varnodes) {
                printObjs.add(new ASTPrintObj(node.getId(), node.getObject().getName(), null ));
                cVars.setConnections(node.getId());
            }
        }

        //Methods
        List<ASTNode> methodnode = methods.getNodes();
        if(!methodnode.isEmpty()){
            for (ASTNode node : methodnode) {
                printObjs.add(new ASTPrintObj(node.getId(), node.getObject().getName(), null, node.getLink().getId() ));
                cMethods.setConnections(node.getId());
                getNextNodes(node.getLink());
            }
        }

        //add Nodes
        printObjs.add(cFinals);
        printObjs.add(cVars);
        printObjs.add(cMethods);

        debugPrintObjects();
    }

    /**
     * iterate trough nodes
     *
     * @param node next node
     */
    private void getNextNodes(ASTNode node){
        ASTPrintObj printObj = new ASTPrintObj(node.getId(), node.getNodeClass().name());
        
        if(node.getNodeClass().equals(ASTClass.INT)){
            printObj.setSecondline(String.valueOf(node.getConstant()));
        }else if (node.getNodeSubclass() != null ){
            printObj.setSecondline(node.getNodeSubclass().name());
        }else if (node.getName() != null ){
            printObj.setSecondline(node.getName());
        }

        if(node.getLeft() != null){
            printObj.setConLeft(node.getLeft().getId());
            getNextNodes(node.getLeft());
        }
        if(node.getRight() != null){
            printObj.setConRight(node.getRight().getId());
            getNextNodes(node.getRight());
        }
        if(node.getLink() != null){
            printObj.setConLink(node.getLink().getId());
            getNextNodes(node.getLink());
        }

        printObjs.add(printObj);
    }

    /**
     * DEBUG - print objects
     */
    private void debugPrintObjects(){
        for (ASTPrintObj obj : printObjs) {
            logger.debug("[ " + obj.getID() + " | "
                    + obj.getFirstline() + " | "
                    + obj.getSecondline() + " | "
                    + obj.getConLink()  + " | "
                    + obj.getConLeft() + " | "
                    + obj.getConRight() + "]");
            if(!obj.getConnections().isEmpty()){
                StringBuilder out = new StringBuilder(("  >  Connections : "));
                for (int z : obj.getConnections()) {
                    out.append(z).append(" | ");
                }
                out.append("\n");
            logger.debug(out);
            }
        }
    }


}
