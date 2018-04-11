package jm.transcribebuddy;

import javafx.util.Duration;

public class Node {
    private String statement;
    Duration start, end;
    
    public Node() {
        statement = "";
    }
    
    public void setStatement(String statement) {
        this.statement = statement;
    }
    
    public String getStatement() {
        return statement;
    }
    
    public int getLength() {
        return statement.length();
    }
}
