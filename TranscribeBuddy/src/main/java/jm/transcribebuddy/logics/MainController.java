package jm.transcribebuddy.logics;

/***   This is the supreme leader of application logics   ***/

import javafx.util.Duration;
import jm.transcribebuddy.dao.ProjectDao;
import jm.transcribebuddy.gui.popups.ProjectForm;

public class MainController {
    final private AppSettings appSettings;
    private ProjectInfo projectInfo;
    private TextBuilder textBuilder;
    static private AudioPlayer audioPlayer;
    final private ProjectDao projectDao;
    
    private boolean workSaved;
    
    public MainController(AppSettings settings) {
        appSettings = settings;
        projectInfo = new ProjectInfo();
        textBuilder = new TextBuilder();
        audioPlayer = new AudioPlayer();
        
        String databaseURL = settings.getDatabaseURL();
        String databaseUser = settings.getDatabaseUser();
        String databasePass = settings.getDatabasePass();
        projectDao = new ProjectDao(databaseURL, databaseUser, databasePass);
        
        workSaved = true;
    }
    
    public boolean isWorkSaved() {
        return workSaved;
    }
    
    /*******            DAO METHODS            *******/
    
    public String getDaoError() {
        return projectDao.getError();
    }
    
    public boolean loadProject(final String textFilePath) {
        // Update project information
        if (projectInfo.setUpFilePaths(textFilePath) == false) {
            return false;
        }
        
        textBuilder = projectDao.load(projectInfo);
        String audioFilePath = projectInfo.getAudioFilePath();
        audioPlayer.openAudioFile(audioFilePath);
        workSaved = true;
        return true;
    }
    
    /**
     * Save project to a text file and project information to the database.
     * 
     * @param textFilePath Absolute path of *.txt file to save
     * @return true if there were no errors during saving
     */
    public boolean saveProject(final String textFilePath) {
        if (textFilePath == null || textFilePath.isEmpty()) {
            return false;
        }
        
        // Create a new text file if it doesn't exist
        boolean newTextFileCreated;
        newTextFileCreated = projectDao.createTextFileIfNotExists(textFilePath);
        
        // Update project information
        if (projectInfo.setUpFilePaths(textFilePath) == false) {
            if (newTextFileCreated) {
                projectDao.removeCreatedTextFile(textFilePath);
            }
            return false;
        }
        
        // Try to save to database
        boolean result = projectDao.save(projectInfo, textBuilder);
        // NOTE TO MYSELF:
        // You might want to check save result before setting workSaved
        workSaved = true;
        return result;
    }
    
    /*******            PROJECT INFO METHODS            *******/
    
    public String getSaveDirectory() {
        return projectInfo.getSaveDirectory();
    }
    
    public String getTextFileName() {
        return projectInfo.getInitialFileName();
    }
    
    public String getProjectName() {
        return projectInfo.getName();
    }
    
    public void editProjectInfo() {
        projectInfo = ProjectForm.show(projectInfo);
    }
    
    /*******            WORD PROCESSING METHODS            *******/
    
    public String getPrevStatement() {
        return textBuilder.getPrev();
    }
    public String getCurrentStatement() {
        return textBuilder.getCurrent();
    }
    public String getNextStatement() {
        return textBuilder.getNext();
    }
    public String getFullText() {
        return textBuilder.getAll();
    }
    public String startTimeToString() {
        return textBuilder.startTimeToString();
    }
    
    public void set(String statement) {
        if (textBuilder.set(statement)) {
            workSaved = false;
        }
    }
    
    public void setStartTime() {
        Duration startTime = audioPlayer.getCurrentTime();
        textBuilder.setStartTime(startTime);
        workSaved = false;
    }
    
    public int parseLastStatement(final String text) {
        int result = textBuilder.parseFromAll(text);
        // result is 0 when nothing has changed
        if (result != 0) {
            workSaved = false;
        }
        return result;
    }
    
    public void deleteCurrentStatement() {
        textBuilder.deleteStatement();
        workSaved = false;
    }
    
    public void selectPrevStatement() {
        textBuilder.selectPrev();
    }
    public void selectNextStatement() {
        textBuilder.selectNext();
    }
    
    public void selectStatementByCaretPosition(final int caretPosition) {
        textBuilder.selectByCaretPosition(caretPosition);
    }
    
    public void endStatement(String statement) {
        Duration startTime = audioPlayer.getCurrentTime();
        textBuilder.endStatement(statement);
        textBuilder.setStartTime(startTime);
        workSaved = false;
    }
    
    public boolean splitStatement(String statement, int splitIndex) {
        Duration startTime = audioPlayer.getCurrentTime();
        boolean result = textBuilder.splitStatement(statement, splitIndex, startTime);
        if (result == true) {
            workSaved = false;
        }
        return result;
    }
    
    
    
    /*******            AUDIO PROCESSING METHODS            *******/
    
    public boolean openAudioFile(final String audioFileURI) {
        if (audioFileURI == null || audioFileURI.isEmpty()) {
            return false;
        }
        audioPlayer.openAudioFile(audioFileURI);
        projectInfo.setAudioFilePath(audioFileURI);
        textBuilder = new TextBuilder();
        workSaved = true;
        return true;
    }
    
    public String getAudioFilePath() {
        return audioPlayer.getFilePath();
    }
    
    public void skipBackward() {
        audioPlayer.skipBackward();
    }
    public void skipBackwardLonger() {
        audioPlayer.skipBackwardLonger();
    }
    public void skipForward() {
        audioPlayer.skipForward();
    }
    public void skipForwardLonger() {
        audioPlayer.skipForwardLonger();
    }
    public void playAudio() {
        audioPlayer.play();
    }
    public void stopAudio() {
        audioPlayer.stop();
    }
    public void changePlayingStatus() {
        audioPlayer.changePlayingStatus();
    }
    
    public void seekBeginningOfAudioTrack() {
        audioPlayer.seekBeginning();
    }
    public void seekBeginningOfCurrentStatement() {
        audioPlayer.seekBeginning(textBuilder);
    }
    
}
