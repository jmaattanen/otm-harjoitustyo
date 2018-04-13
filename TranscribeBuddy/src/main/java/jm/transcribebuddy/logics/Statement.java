package jm.transcribebuddy.logics;

import javafx.util.Duration;

public class Statement {
    private String statement;
    Duration start;
    
    public Statement() {
        statement = "";
    }
    
    public Statement(String statement) {
        this.statement = statement;
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
