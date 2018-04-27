package jm.transcribebuddy.logics.storage;

public class AppSettings {
    final private String databaseURL;
    final private String databaseUser;
    final private String databasePass;
    
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
