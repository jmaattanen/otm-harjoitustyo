package jm.transcribebuddy.logics;

public class ProjectInfo {
    private int id;
    private String name;
    private String description;
    private String textFilePath;
    private String audioFilePath;
    
    public ProjectInfo() {
        id = 1;
        name = "My Test Project";
        description = "The application is being developed.";
        textFilePath = "";
        audioFilePath = "";
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
}
