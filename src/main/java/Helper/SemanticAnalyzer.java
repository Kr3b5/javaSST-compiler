package Helper;

import AST.AST;
import AST.ASTClass;
import AST.ASTNode;
import AST.ASTNodeContainer;
import Data.STType;
import Data.TokenType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO:  [ ]  Operanden typkompatibel?
// TODO:  [X]  schleifen bzw. if:  boolescher wert?
// TODO:  [ ]  typen der Operanden == typen der funktionsparameter ?
// TODO:  [X]  auf jeden Pfad, über den eine Methode verlassen werden kann: gibt ein return etwas zurück? Das kann rekursiv von hinten überprüft werden.
//             Die letzte Anweisung muss ein return sein oder falls die letzte Anweisung ein if ist, muss der letzte Eintrag im if und else ein return sein
// TODO:  [ ]  Konstanten Ausdrücke ausgewertet?
// TODO:  [ ]  lokale Variablen müssen definiert wurden, bevor sie benutzt werden


public class SemanticAnalyzer {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(SemanticAnalyzer.class.getName());

    //globals
    private boolean errorFound = false;
    private ASTNode bufferNode;
    private String actualCheck;


    public boolean analyze(AST ast) {

        ASTNodeContainer methods = ast.getMethods();
        for (ASTNode method : methods.getNodes()) {

            checkMethodReturn(method);

            checkallNodes(method);

        }

        return errorFound;
    }


    //-------------------------------------------------------------------------------------------------------
    // Check : INT methods must end with return
    private void checkMethodReturn(ASTNode method) {
        actualCheck = "checkMethodReturn";
        if (method.getObject().getSTType().equals(STType.INT)) {
            logInfo("Check INT method for return: " + method.getObject().getName());
            getLastLinkNode(method.getLink());
            // if last node = IF_ELSE then both trees must end with RETURN node
            if (bufferNode.getNodeClass().equals(ASTClass.IF_ELSE)) {
                logInfo("IF_ELSE node found");
                ASTNode ifelseNode = bufferNode;

                // IF Node
                getLastLinkNode(ifelseNode.getLeft().getRight());
                if (bufferNode.getNodeClass().equals(ASTClass.RETURN)) {
                    logInfo("IF node - RETURN node found");
                } else {
                    logError("IF node - RETURN node not found [" + "Last node: " + bufferNode.getNodeClass() + "]");
                    errorFound = true;
                }

                // ELSE Node
                getLastLinkNode(ifelseNode.getRight());
                if (bufferNode.getNodeClass().equals(ASTClass.RETURN)) {
                    logInfo("ELSE node - RETURN node found");
                } else {
                    logError("ELSE node - RETURN node not found [" + "Last node: " + bufferNode.getNodeClass() + "]");
                    errorFound = true;
                }
            } else if (bufferNode.getNodeClass().equals(ASTClass.RETURN)) {
                logInfo("RETURN node found");
            } else {
                logError("RETURN node not found [" + "Last node: " + bufferNode.getNodeClass() + "]");
                errorFound = true;
            }
        }

    }

    // get last linked node
    private void getLastLinkNode(ASTNode node) {
        if (node.getLink() == null) {
            bufferNode = node;
        } else {
            getLastLinkNode(node.getLink());
        }
    }

    //-------------------------------------------------------------------------------------------------------


    private void checkallNodes(ASTNode method) {

        //check IF & WHILE has bool condition
        if (method.getNodeClass() != null && (method.getNodeClass().equals(ASTClass.IF) ||
                method.getNodeClass().equals(ASTClass.WHILE))) {
            checkIfWhileCondition(method);
        }


        if (method.getLeft() != null) {
            checkallNodes(method.getLeft());
        }
        if (method.getRight() != null) {
            checkallNodes(method.getRight());
        }
        if (method.getLink() != null) {
            checkallNodes(method.getLink());
        }
    }

    // Check : IF & WHILE condition = bool
    private void checkIfWhileCondition(ASTNode ifWhileNode) {
        actualCheck = "checkIfWhileCondition";
        logInfo("Check " + ifWhileNode.getNodeClass() + " Node (ID: " + ifWhileNode.getId() + ")");
        ASTNode conditionNode = ifWhileNode.getLeft();
        if (conditionNode.getNodeSubclass() != null &&
                (conditionNode.getNodeSubclass().equals(TokenType.EQUAL)   ||
                 conditionNode.getNodeSubclass().equals(TokenType.NEQUAL)  ||
                 conditionNode.getNodeSubclass().equals(TokenType.GREATER) ||
                 conditionNode.getNodeSubclass().equals(TokenType.GR_EQ)   ||
                 conditionNode.getNodeSubclass().equals(TokenType.SMALLER) ||
                 conditionNode.getNodeSubclass().equals(TokenType.SM_EQ))) {
            logInfo("Bool condition found");
        } else {
            errorFound = true;
            logError("Bool condition not found [node: " + conditionNode.getNodeClass() + "]");
        }
    }


    //-------------------------------------------------------------------------------------------------------
    // Helper Methods

    private void logError(String message) {
        logger.error("SemanticAnalyzer." + actualCheck + ": " + message);
    }

    private void logInfo(String message) {
        logger.info("SemanticAnalyzer." + actualCheck + ": " + message);
    }
}
