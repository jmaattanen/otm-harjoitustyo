package jm.transcribebuddy.logics;

import javafx.util.Duration;

public class Statement {
    private String statement;
    private Duration start;
    
    public Statement() {
        statement = "";
        start = Duration.seconds(0);
    }
    
    public Statement(Duration startTime) {
        statement = "";
        start = startTime;
    }
    
    public Statement(String statement, Duration startTime) {
        this.statement = statement;
        this.start = startTime;
    }
    
    public void set(String statement) {
        this.statement = statement;
    }
    
    public void setStartTime(Duration startTime) {
        start = startTime;
    }
    
    public Duration getStartTime() {
        return start;
    }
    
    @Override
    public String toString() {
        return statement;
    }
    
    public int getLength() {
        return statement.length();
    }
    
}
