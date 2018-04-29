package jm.transcribebuddy.logics.storage;

/***   A single node object of sentences managed by TextBuilder   ***/

import javafx.util.Duration;

public class Statement {
    private String statement;
    private Duration startTime;
    private Category subcategory;
    
    public Statement(Category subcategory) {
        statement = "";
        startTime = Duration.seconds(0);
        this.subcategory = subcategory;
        if (subcategory != null) {
            subcategory.addStatement();
        }
    }
    
    public Statement(Duration startTime, Category subcategory) {
        statement = "";
        this.startTime = startTime;
        checkStartTime();
        this.subcategory = subcategory;
        if (subcategory != null) {
            subcategory.addStatement();
        }
    }
    
    public Statement(String statement, Duration startTime, Category subcategory) {
        if (statement == null) {
            this.statement = "";
        } else {
            this.statement = statement.trim();
        }
        this.startTime = startTime;
        checkStartTime();
        this.subcategory = subcategory;
        if (subcategory != null) {
            subcategory.addStatement();
        }
    }
    
    public int getLength() {
        return statement.length();
    }
    
    public Category getSubcategory() {
        return subcategory;
    }
    
    public Duration getStartTime() {
        return startTime;
    }
    
    /**
     * Method copies the trimmed string to the Statement instance.
     * NULL won't affect.
     * 
     * @param statement 
     */
    public void set(String statement) {
        if (statement != null) {
            this.statement = statement.trim();
        }
    }
    
    public void setSubcategory(Category subcategory) {
        if (subcategory != null) {
            this.subcategory = subcategory;
        }
    }
    
    public boolean isInSubcategory(Category subcategory) {
        return this.subcategory == subcategory;
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
    
}
