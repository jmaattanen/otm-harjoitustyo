package jm.transcribebuddy.logics;

import javafx.util.Duration;

public class MockPlayer implements Player {
    boolean isSet, isPlaying;
    Duration position;
    String audioURI;
    
    public MockPlayer() {
        isSet = false;
        isPlaying = false;
        position = Duration.ZERO;
        audioURI = "No audio";
    }
    
    @Override
    public boolean isSet() {
        return isSet;
    }
    
    @Override
    public void openAudioFile(String audioFileURI) {
        if (AudioPlayer.isSupportedURI(audioFileURI)) {
            isSet = true;
            isPlaying = false;
            position = Duration.ZERO;
            audioURI = audioFileURI;
        }
    }
    
    @Override
    public String getFileURI() {
        return audioURI;
    }
    
    private void simulatePlaying() {
        if (isPlaying) {
            position = position.add(Duration.seconds(1));
        }
    }
    
    @Override
    public Duration getCurrentTime() {
        Duration time = position;
        simulatePlaying();
        return time;
    }
    
    @Override
    public void play() {
        isPlaying = true;
        simulatePlaying();
    }
    
    @Override
    public void stop() {
        isPlaying = false;
    }
    
    @Override
    public void changePlayingStatus() {
        if (isPlaying) {
            stop();
        } else {
            play();
        }
    }
    
    @Override
    public void seekBeginning() {
        position = Duration.ZERO;
        simulatePlaying();
    }
    
    private void checkPosition() {
        if (position.lessThan(Duration.ZERO)) {
            position = Duration.ZERO;
        }
    }
    
    @Override
    public void seekTimeMark(Duration timeMark) {
        position = timeMark;
        checkPosition();
        simulatePlaying();
    }
    
    @Override
    public void skipBackward() {
        position = position.subtract(Duration.seconds(5));
        checkPosition();
    }
    
    @Override
    public void skipBackwardLonger() {
        position = position.subtract(Duration.seconds(30));
        checkPosition();
    }
    
    @Override
    public void skipForward() {
        position = position.add(Duration.seconds(5));
        checkPosition();
    }
    
    @Override
    public void skipForwardLonger() {
        position = position.add(Duration.seconds(30));
        checkPosition();
    }
    
    @Override
    public boolean isPlaying() {
        return isPlaying;
    }
    
    
}
