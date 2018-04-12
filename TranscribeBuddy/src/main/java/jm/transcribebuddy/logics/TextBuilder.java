package jm.transcribebuddy.logics;

import java.util.ArrayList;

public class TextBuilder {
    private ArrayList<Statement> statements;
    private int workingIndex;
    
    public TextBuilder() {
        statements = new ArrayList<>();
        Statement firstStatement = new Statement();
        statements.add(firstStatement);
        workingIndex = 0;
    }
    
    public String getCurrent() {
//        if( workingIndex >= statements.size())
//            workingIndex = statements.size()-1;
        return statements.get(workingIndex).toString();
    }
    
    public String getPrev() {
        if(workingIndex == 0)
            return "*Projektin alku*";
        return statements.get(workingIndex-1).toString();
    }
    
    public String getNext() {
        if(workingIndex == statements.size()-1)
            return "*Projektin loppu*";
        return statements.get(workingIndex+1).toString();
    }
    
    public String getLast() {
        int lastIndex = statements.size()-1;
        return statements.get(lastIndex).toString();
    }
    
    public String getSecondLast() {
        int index = statements.size()-2;
        if(index < 0)
            return "";
        return statements.get(index).toString();
    }
    
    public String getAll() {
        if( statements.isEmpty() )
            return null;
        String text = "";
        for(Statement node : statements)
            text += node.toString() + " ";
        return text;
    }
    
    public void set(String statement) {
        Statement node = statements.get(workingIndex);
        node.set(statement.trim());
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
        Statement node = statements.get(workingIndex);
        node.set(statement);
        if(workingIndex == statements.size()-1) {
            // add a new node to the end
            statements.add(new Statement());
            workingIndex++;
        }
        else {
            // insert a new node
        }
    }
}
