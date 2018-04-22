package jm.transcribebuddy.logics;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import jm.transcribebuddy.dao.ProjectDao;

public class MainController {
    private TextBuilder textBuilder;
    static private AudioPlayer audioPlayer;
    private ProjectDao projectDao;
    private ProjectInfo projectInfo;
    
    public MainController() {
        textBuilder = new TextBuilder();
        audioPlayer = new AudioPlayer();
        projectDao = new ProjectDao();
        projectInfo = new ProjectInfo();
    }
    
    /*******            DAO METHODS            *******/
    
    public boolean loadProject() {
        this.stopAudio();
        final int projectId = projectInfo.getId();
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
        textBuilder = projectDao.readFile(projectId, textFilePath);
        // Update project information
        projectInfo.setUpFilePaths(file);
        return true;
    }
    
    public boolean saveProject() {
        this.stopAudio();
        final int projectId = projectInfo.getId();
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
        final String textFilePath = file.toString();
        projectDao.save(projectId, textFilePath, textBuilder);
        // Update project information
        projectInfo.setUpFilePaths(file);
        return true;
    }
    
    /*******            PROJECT INFO METHODS            *******/
    
    public String getProjectName() {
        return projectInfo.getName();
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
    
    public void setCurrentStatement(String statement) {
        textBuilder.set(statement);
    }
    
    public void set(String statement) {
        textBuilder.set(statement);
    }
    
    public void setStartTime() {
        Duration startTime = audioPlayer.getCurrentTime();
        textBuilder.setStartTime(startTime);
    }
    
    public boolean parseLastStatement(final String text) {
        boolean result = textBuilder.parseFromAll(text);
        return result;
    }
    
    public void deleteCurrentStatement() {
        textBuilder.deleteStatement();
    }
    
    public void selectPrevStatement() {
        textBuilder.selectPrev();
    }
    public void selectNextStatement() {
        textBuilder.selectNext();
    }
    
    public void endStatement(String statement) {
        Duration startTime = audioPlayer.getCurrentTime();
        textBuilder.endStatement(statement);
        textBuilder.setStartTime(startTime);
    }
    
    public boolean splitStatement(String statement, int splitIndex) {
        Duration startTime = audioPlayer.getCurrentTime();
        return textBuilder.splitStatement(statement, splitIndex, startTime);
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
        textBuilder = new TextBuilder();
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
