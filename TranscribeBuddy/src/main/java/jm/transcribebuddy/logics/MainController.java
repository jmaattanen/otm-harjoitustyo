package jm.transcribebuddy.logics;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import jm.transcribebuddy.dao.ProjectDao;
import jm.transcribebuddy.gui.ProjectForm;

public class MainController {
    private AppSettings appSettings;
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
    
    public boolean loadProject() {
        this.stopAudio();
        String saveDirectory = projectInfo.getSaveDirectory();
        // Open dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("Select a TXT file (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File(saveDirectory));
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            // Load canceled
            return false;
        }
        // Load project
        final String textFilePath = file.toString();
        textBuilder = projectDao.readFile(projectInfo, textFilePath);
        String audioFilePath = projectInfo.getAudioFilePath();
        audioPlayer.openAudioFile(audioFilePath);
        workSaved = true;
        return true;
    }
    
    public boolean saveProject() {
        this.stopAudio();
        final String saveDirectory = projectInfo.getSaveDirectory();
        final String initialFileName = projectInfo.getInitialFileName();
        // Save dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialDirectory(new File(saveDirectory));
        fileChooser.setInitialFileName(initialFileName);
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            // Save canceled
            return false;
        }
        // Save project
        boolean result = saveProject(file);
        return result;
    }
    
    private boolean saveProject(File textFile) {
        final String textFilePath = textFile.toString();
        boolean result = projectDao.save(projectInfo, textFilePath, textBuilder);
        // Update project information
        projectInfo.setUpFilePaths(textFile);
        workSaved = true;
        return result;
    }
    
    /*******            PROJECT INFO METHODS            *******/
    
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
        workSaved = false;
        return result;
    }
    
    
    
    /*******            AUDIO PROCESSING METHODS            *******/
    
    public boolean openAudioFile() {
        this.stopAudio();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open audio file");
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter(
                        "Select a file (*.m4a),(*.mp3),(*.wav)",
                        "*.m4a", "*.mp3", "*.wav");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return false;
        }
        String audioFilePath = file.toURI().toString();
        audioPlayer.openAudioFile(audioFilePath);
        projectInfo.setAudioFilePath(audioFilePath);
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
