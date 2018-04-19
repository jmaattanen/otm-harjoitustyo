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
    
    public void loadProject() {
        textBuilder = projectDao.readFile();
    }
    public void saveProject() {
        final int projectId = projectInfo.getId();
        projectDao.save(projectId, textBuilder);
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
    
    public void setStartTime() {
        Duration startTime = audioPlayer.getCurrentTime();
        textBuilder.setStartTime(startTime);
    }
    
    public void parseLastStatement(final String text) {
        textBuilder.parseFromAll(text);
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
        stopAudio();
        FileChooser fileChooser = new FileChooser();
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
