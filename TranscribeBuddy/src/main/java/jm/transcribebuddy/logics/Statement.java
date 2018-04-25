package jm.transcribebuddy.logics;

/***   A single node object of sentences   ***/

import javafx.util.Duration;

public class Statement {
    private String statement;
    private Duration startTime;
    
    public Statement() {
        statement = "";
        startTime = Duration.seconds(0);
    }
    
    public Statement(Duration startTime) {
        statement = "";
        this.startTime = startTime;
        checkStartTime();
    }
    
    public Statement(String statement, Duration startTime) {
        this.statement = statement;
        this.startTime = startTime;
    }
    
    public void set(String statement) {
        if (statement != null) {
            this.statement = statement;
        }
    }
    
    private void checkStartTime() {
        if (startTime.lessThan(Duration.ZERO)) {
            startTime = Duration.ZERO;
        }
    }
    
    public void setStartTime(Duration startTime) {
        this.startTime = startTime;
        checkStartTime();
    }
    public void setStartTime(double startTimeInMillis) {
        startTime = Duration.millis(startTimeInMillis);
        checkStartTime();
    }
    
    public Duration getStartTime() {
        return startTime;
    }
    
    public double startTimeToDouble() {
        return startTime.toMillis();
    }
    
    public String startTimeToString() {
        Integer seconds;
        seconds = (int) (startTime.toSeconds());
        String time = "";
        if (seconds >= 3600) {
            time += hoursToString(seconds) + ":";
        }
        time += minutesToString(seconds) + ":" + secondsToString(seconds);
        return time;
    }
    private String hoursToString(int seconds) {
        if (seconds >= 3600) {
            return Integer.toString(seconds / 3600);
        }
        return "";
    }
    private String minutesToString(int seconds) {
        seconds = seconds % 3600;
        String time = "";
        if (seconds < 600) {
            time += "0";
        }
        time += Integer.toString(seconds / 60);
        return time;
    }
    private String secondsToString(int seconds) {
        String time = "";
        seconds = seconds % 60;
        if (seconds < 10) {
            time += "0";
        }
        time += Integer.toString(seconds);
        return time;
    }
    
    @Override
    public String toString() {
        return statement;
    }
    
    public int getLength() {
        return statement.length();
    }
    
}
