package jm.transcribebuddy;

import java.util.ArrayList;

public class TextBuilder {
    private ArrayList<Node> statements;
    private int workingIndex;
    
    public TextBuilder() {
        statements = new ArrayList<>();
        Node first = new Node();
        statements.add(first);
        workingIndex = 0;
    }
    
    public String getCurrent() {
        if( statements.isEmpty() )
            return null;
        if( workingIndex >= statements.size())
            workingIndex = statements.size()-1;
        return statements.get(workingIndex).getStatement();
    }
    
    public String getPrev() {
        if( statements.isEmpty() )
            return null;
        if(workingIndex == 0)
            return "*Projektin alku*";
        return statements.get(workingIndex-1).getStatement();
    }
    
    public String getNext() {
        if( statements.isEmpty() )
            return null;
        if(workingIndex == statements.size()-1)
            return "*Projektin loppu*";
        return statements.get(workingIndex+1).getStatement();
    }
    
    public String getAll() {
        if( statements.isEmpty() )
            return null;
        String text = "";
        for(Node node : statements)
            text += node.getStatement() + " ";
        return text;
    }
    
    public void set(String statement) {
        //statements.set(workingIndex, statement);
        Node node = statements.get(workingIndex);
        node.setStatement(statement);
    }
    
    public void endStatement(String statement) {
        Node node = statements.get(workingIndex);
        node.setStatement(statement);
        if(workingIndex == statements.size()-1) {
            // add a new node to the end
            node = new Node();
            statements.add(node);
            workingIndex++;
        }
        else {
            // insert a new node
        }
    }
}
