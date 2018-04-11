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
//        if( workingIndex >= statements.size())
//            workingIndex = statements.size()-1;
        return statements.get(workingIndex).getStatement();
    }
    
    public String getPrev() {
        if(workingIndex == 0)
            return "*Projektin alku*";
        return statements.get(workingIndex-1).getStatement();
    }
    
    public String getNext() {
        if(workingIndex == statements.size()-1)
            return "*Projektin loppu*";
        return statements.get(workingIndex+1).getStatement();
    }
    
    public String getLast() {
        int lastIndex = statements.size()-1;
        return statements.get(lastIndex).getStatement();
    }
    
    public String getSecondLast() {
        int index = statements.size()-2;
        if(index < 0)
            return "";
        return statements.get(index).getStatement();
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
        Node node = statements.get(workingIndex);
        node.setStatement(statement.trim());
    }
    
    public void setLast(String statement) {
        workingIndex = statements.size()-1;
        this.set(statement.trim());
    }
    
    public void endStatement(String statement) {
        // delete whitespaces in the end of statement
        for( int i = statement.length()-1; i >= 0 && statement.charAt(i) == ' '; i-- ) {
            statement = statement.substring(0, i);
        }
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
