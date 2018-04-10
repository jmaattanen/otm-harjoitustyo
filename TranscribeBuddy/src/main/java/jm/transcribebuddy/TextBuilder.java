package jm.transcribebuddy;

import java.util.ArrayList;

public class TextBuilder {
    private ArrayList<String> statements;
    private int workingIndex;
    
    public TextBuilder() {
        statements = new ArrayList<>();
        String firstLine = new String();
        statements.add(firstLine);
        workingIndex = 0;
    }
    
    public String getCurrent() {
        if( statements.isEmpty() )
            return null;
        if( workingIndex >= statements.size())
            workingIndex = statements.size()-1;
        return statements.get(workingIndex);
    }
    
    public String getPrev() {
        if( statements.isEmpty() )
            return null;
        if(workingIndex == 0)
            return "*Projektin alku*";
        return statements.get(workingIndex-1);
    }
    
    public String getNext() {
        if( statements.isEmpty() )
            return null;
        if(workingIndex == statements.size()-1)
            return "*Projektin loppu*";
        return statements.get(workingIndex+1);
    }
    
    public String getAll() {
        if( statements.isEmpty() )
            return null;
        return statements.get(statements.size()-1);
    }
    
    public void set(String statement) {
        statements.set(workingIndex, statement);
    }
}
