package AbstractSyntaxTree;

import java.util.LinkedList;
import java.util.List;

public class ASTPrintObj {

    private int ID;

    private String firstline;
    private String secondline;

    private int conLink;
    private int conLeft;
    private int conRight;

    private List<Integer> connections = new LinkedList<Integer>();

    public ASTPrintObj(int ID, String firstline, String secondline, int conLink, int conLeft, int conRight) {
        this.ID = ID;
        this.firstline = firstline;
        this.secondline = secondline;
        this.conLink = conLink;
        this.conLeft = conLeft;
        this.conRight = conRight;
    }

    public ASTPrintObj(int ID, String firstline, String secondline, int conLink) {
        this.ID = ID;
        this.firstline = firstline;
        this.secondline = secondline;
        this.conLink = conLink;
    }

    public ASTPrintObj(int ID, String firstline, String secondline) {
        this.ID = ID;
        this.firstline = firstline;
        this.secondline = secondline;
    }

    public ASTPrintObj(int ID, String firstline) {
        this.ID = ID;
        this.firstline = firstline;
    }



    public void setSecondline(String secondline) {
        this.secondline = secondline;
    }

    public void setConLink(int conLink) {
        this.conLink = conLink;
    }

    public void setConLeft(int conLeft) {
        this.conLeft = conLeft;
    }

    public void setConRight(int conRight) {
        this.conRight = conRight;
    }

    public int getID() {
        return ID;
    }

    public String getFirstline() {
        return firstline;
    }

    public String getSecondline() {
        return secondline;
    }

    public int getConLink() {
        return conLink;
    }

    public int getConLeft() {
        return conLeft;
    }

    public int getConRight() {
        return conRight;
    }

    public void setConnections(int connection) {
        this.connections.add(connection);
    }

    public List<Integer> getConnections() {
        return connections;
    }
}
