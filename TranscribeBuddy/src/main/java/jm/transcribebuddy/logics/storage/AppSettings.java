package jm.transcribebuddy.logics.storage;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class AppSettings {
    final private String databaseURL;
    final private String databaseUser;
    final private String databasePass;
    final private String documentHome;
    
    public AppSettings(String databaseURL, String databaseUser, String databasePass) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePass = databasePass;
        documentHome = File.separator;
    }
    
    public AppSettings(Properties properties)  {
        databaseURL = properties.getProperty("databaseURL");
        databaseUser = properties.getProperty("postgresUser");
        databasePass = properties.getProperty("postgresPass");
        String docHomePath = properties.getProperty("documentHome");
        documentHome = getValidHome(docHomePath);
        
    }
    
    private String getValidHome(String docHomePath) {
        if (docHomePath != null) {
            File file = new File(docHomePath);
            if (file.exists() && file.isDirectory()) {
                return docHomePath;
            }
        }
        return File.separator;
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
    
    public String getDocumentHomePath() {
        return documentHome;
    }
}
