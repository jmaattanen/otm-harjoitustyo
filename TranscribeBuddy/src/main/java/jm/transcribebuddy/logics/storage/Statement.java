package jm.transcribebuddy.logics.storage;

import javafx.util.Duration;

/**
 * A single node object of sentences managed by TextBuilder.
 * 
 * @author Juha
 */
public class Statement {
    private String statement;
    private Duration startTime;
    private Category subcategory;
    
    /**
     * Creates a new Statement object with empty phrase and zero time mark.
     * 
     * @see jm.transcribebuddy.logics.storage.Category
     * @param subcategory The sub category to which the statement is added.
     * Can be null.
     */
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
     * 
     * @param statement NULL won't affect.
     */
    public void set(String statement) {
        if (statement != null) {
            this.statement = statement.trim();
        }
    }
    
    /**
     * Method puts this statement into the given sub category and
     * update category sizes.
     * 
     * @see jm.transcribebuddy.logics.storage.Category
     * @param subcategory NULL won't affect.
     */
    public void setSubcategory(Category subcategory) {
        if (subcategory != null) {
            if (this.subcategory != null) {
                // deduct statement from the counter of the old category
                this.subcategory.removeStatement();
            }
            this.subcategory = subcategory;
            this.subcategory.addStatement();
        }
    }
    
    /**
     * Method checks if this statement belongs to given sub category.
     * 
     * @see jm.transcribebuddy.logics.storage.Category
     * @param subcategory Comparable sub category.
     * @return True if belongs to given sub category.
     */
    public boolean isInSubcategory(Category subcategory) {
        return this.subcategory == subcategory;
    }
    
    private void checkStartTime() {
        if (startTime.lessThan(Duration.ZERO)) {
            startTime = Duration.ZERO;
        }
    }
    
    /**
     * The time mark of this statement can be set by this method.
     * 
     * @param startTime Negative values ​​will be changed to zero.
     */
    public void setStartTime(Duration startTime) {
        this.startTime = startTime;
        checkStartTime();
    }
    /**
     * The time mark of this statement can be set by this method.
     * 
     * @param startTimeInMillis Time mark in milliseconds.
     * Negative values ​​will be changed to zero.
     */
    public void setStartTime(double startTimeInMillis) {
        startTime = Duration.millis(startTimeInMillis);
        checkStartTime();
    }
    
    public double startTimeToDouble() {
        return startTime.toMillis();
    }
    
    /**
     * This method returns the time mark of this statement.
     * 
     * @return String in "H:MM:SS" format.
     */
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
