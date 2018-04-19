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
    public void setStartTime(double startTimeInMillis) {
        start = Duration.millis(startTimeInMillis);
        if (start.lessThan(Duration.ZERO)) {
            start = Duration.ZERO;
        }
    }
    
    public Duration getStartTime() {
        return start;
    }
    
    public double startTimeToDouble() {
        return start.toMillis();
    }
    
    public String startTimeToString() {
        Integer seconds;
        seconds = (int) (start.toSeconds());
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
