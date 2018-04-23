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
    
    public String getDescription() {
        return description;
    }
    
    public String getSaveDirectory() {
        return saveDirectory;
    }
    
    public String getTextFilePath() {
        if (textFileName == null) {
            return "";
        }
        String textFilePath = saveDirectory + "\\" + textFileName;
        return textFilePath;
    }
    
    public String getAudioFilePath() {
        return audioFilePath;
    }
    
    public String getInitialFileName() {
        if (textFileName == null || textFileName.isEmpty()) {
            return "*.txt";
        }
        return textFileName;
    }
    
    public void setProjectName(String name) {
        if (name != null && !name.isEmpty() && name.length() < 30) {
            this.name = name;
        }
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
    
    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }
}
