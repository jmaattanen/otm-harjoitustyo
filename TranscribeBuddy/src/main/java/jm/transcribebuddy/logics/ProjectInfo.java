package jm.transcribebuddy.logics;

import java.io.File;

public class ProjectInfo {
    private int id;
    private String name;
    private String description;
    private String textFileName;
    private String audioFilePath;
    
    private String saveDirectory;
    
    public ProjectInfo() {
        id = 1;
        name = "My Test Project";
        description = "The application is being developed.";
        textFileName = null;
        audioFilePath = "";
        saveDirectory = "\\";
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSaveDirectory() {
        return saveDirectory;
    }
    
    public String getTextFilePath() {
        String textFilePath = saveDirectory + "\\" + textFileName;
        return textFilePath;
    }
    
    public String getInitialFileName() {
        if (textFileName == null || textFileName.isEmpty()) {
            return "*.txt";
        }
        return textFileName;
    }
    
    public void setSaveDirectory(final String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }
    
    public void setTextFilePath(final String textFilePath) {
        this.textFileName = textFilePath;
    }
    
    public void setUpFilePaths(File textFile) {
        if (textFile != null) {
            saveDirectory = textFile.getParent();
            textFileName = textFile.getName();
        }
    }
}
