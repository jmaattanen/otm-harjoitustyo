package jm.transcribebuddy.logics;

import javafx.util.Duration;

/**
 *
 * @author juham
 */
public interface Player {
    
    public boolean isSet();
    public void openAudioFile(String audioFileURI);
    public String getFileURI();
    public Duration getCurrentTime();
    public void play();
    public void stop();
    public void changePlayingStatus();
    public void seekBeginning();
    public void seekTimeMark(Duration timeMark);
    public void skipBackward();
    public void skipBackwardLonger();
    public void skipForward();
    public void skipForwardLonger();
    public boolean isPlaying();
    
}
