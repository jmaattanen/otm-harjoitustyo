package jm.transcribebuddy.logics;

import javafx.util.Duration;

public class Statement {
    private String statement;
    Duration start, end;
    
    public Statement() {
        statement = "";
    }
    
    public void set(String statement) {
        this.statement = statement;
    }
    
    @Override
    public String toString() {
        return statement;
    }
    
    public int getLength() {
        return statement.length();
    }
    
}
