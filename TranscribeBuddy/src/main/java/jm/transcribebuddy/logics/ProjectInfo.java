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
        id = 0;
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
    
    public boolean isNewProject() {
        return id == 0;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean setProjectName(String name) {
        if (name != null && !name.isEmpty() && name.length() < 30) {
            this.name = name;
            return true;
        }
        return false;
    }
    
    public boolean setDescription(String description) {
        if (description == null) {
            this.description = "";
            return true;
        } else if (description.length() <= 512) {
            this.description = description;
            return true;
        }
        return false;
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
    
    public void setUpFilePaths(String textFilePath) {
        File file = new File(textFilePath);
        setUpFilePaths(file);
    }
    
    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }
}
