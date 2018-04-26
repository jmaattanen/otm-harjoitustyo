package jm.transcribebuddy.logics;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectInfo {
    private int id;
    private String name;
    private String description;
    private String textFileName;
    private String audioFilePath;
    private String saveDirectory;
    
    final private int maxNameLength = 30;
    final private int maxDescriptionLength = 512;
    
    public ProjectInfo() {
        id = 0;
        name = "New Project";
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date today = new Date();
        description = "Created " + dateFormat.format(today);
        textFileName = null;
        audioFilePath = "";
        saveDirectory = "\\";
    }
    
    public ProjectInfo(ProjectInfo other) {
        id = other.id;
        name = other.name;
        description = other.description;
        textFileName = other.textFileName;
        audioFilePath = other.audioFilePath;
        saveDirectory = other.saveDirectory;
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
        if (name == null || name.isEmpty()) {
            return false;
        } else if (name.length() > maxNameLength) {
            this.name = name.substring(0, maxNameLength);
        } else {
            this.name = name;
        }
        return true;
    }
    
    public boolean setDescription(String description) {
        if (description == null) {
            return false;
        } else if (description.length() > maxDescriptionLength) {
            this.description = description.substring(0, maxDescriptionLength);
        } else {
            this.description = description;
        }
        return true;
    }
    
    public void setSaveDirectory(final String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }
    
    public void setTextFilePath(final String textFilePath) {
        this.textFileName = textFilePath;
    }
    
    private boolean setUpFilePaths(File textFile) {
        if (textFile == null || !textFile.isFile()) {
            return false;
        }
        String parentPath = textFile.getParent();
        if (parentPath == null) {
            return false;
        }
        File dir = new File(parentPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
        // path ok
        saveDirectory = textFile.getParent();
        textFileName = textFile.getName();
        return true;
    }
    
    public boolean setUpFilePaths(final String textFilePath) {
        if (textFilePath == null || textFilePath.lastIndexOf(".txt") == -1) {
            return false;
        }
        File file = new File(textFilePath);
        return setUpFilePaths(file);
    }
    
    public boolean setAudioFilePath(final String audioFilePath) {
        if (audioFilePath == null) {
            return false;
        }
        this.audioFilePath = audioFilePath;
        return true;
    }
}
