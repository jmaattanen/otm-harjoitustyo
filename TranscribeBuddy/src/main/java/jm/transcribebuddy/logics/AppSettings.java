package jm.transcribebuddy.logics;

public class AppSettings {
    private String databaseURL;
    private String databaseUser;
    private String databasePass;
    
    public AppSettings(String databaseURL, String databaseUser, String databasePass) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePass = databasePass;
    }
    
    public String getDatabaseURL() {
        return databaseURL;
    }
    
    public String getDatabaseUser() {
        return databaseUser;
    }
    
    public String getDatabasePass() {
        return databasePass;
    }
}
