package Data;

import java.util.LinkedList;

public class SymbolTable {

    LinkedList<STObject> objects;

    SymbolTable enclose;


    public SymbolTable() {
        this.objects = new LinkedList<>();
    }

    public SymbolTable(SymbolTable enclose) {
        this.objects = new LinkedList<>();
        this.enclose = enclose;
    }


    public LinkedList<STObject> getObjects() {
        return objects;
    }

    public SymbolTable getEnclose() {
        return enclose;
    }

    public Boolean insert(STObject obj) {
        if(objects.contains(obj)){
            return false;
        }
        objects.add(obj);
        return true;
    }


    public STObject find(String name){
        for (STObject object: objects) {
            if(object.getName().equals(name) ){
                return object;
            }
        }
        return null;
    }


}
