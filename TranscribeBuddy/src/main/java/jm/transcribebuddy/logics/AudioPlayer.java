package jm.transcribebuddy.logics;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer {
    
    private String audioFilePath;
    private MediaPlayer mediaPlayer;
    private Duration skipTime, longerSkipTime;
    
    public AudioPlayer() {
        audioFilePath = "Ei audiota";
        skipTime = Duration.seconds(5);
        longerSkipTime = Duration.seconds(30);
    }
    
    public AudioPlayer(String filePath) {
        if (filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            audioFilePath = filePath;
        } else {
            audioFilePath = "Ei audiota";
        }
        skipTime = Duration.seconds(5);
        longerSkipTime = Duration.seconds(30);
    }
    
    public void openAudioFile(String filePath) {
        this.stop();
        if (filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            audioFilePath = filePath;
        }
    }
    
    public String getFilePath() {
        return audioFilePath;
    }
    
    public Duration getCurrentTime() {
        if (mediaPlayer == null) {
            return Duration.seconds(0);
        }
        return mediaPlayer.getCurrentTime();
    }
    
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
    
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    
    public void changePlayingStatus() {
        if (mediaPlayer != null) {
            if (isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }
    
    public void seekBeginning() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(0));
        }
    }
    
    public void seekBeginning(TextBuilder textBuilder) {
        if (mediaPlayer != null) {
            Duration startTime = textBuilder.getStartTime();
            mediaPlayer.seek(startTime);
        }
    }
    
    public void skipBackward() {
        if (mediaPlayer != null) {
            skipBackward(skipTime);
        }
    }
    public void skipBackwardLonger() {
        if (mediaPlayer != null) {
            skipBackward(longerSkipTime);
        }
    }
    private void skipBackward(Duration interval) {
        Duration duration = mediaPlayer.getCurrentTime();
        if (duration.greaterThan(interval)) {
            duration = duration.subtract(interval);
        } else {
            duration = mediaPlayer.getStartTime();
        }
        mediaPlayer.seek(duration);
    }
    
    public void skipForward() {
        if (mediaPlayer != null) {
            skipForward(skipTime);
        }
    }
    public void skipForwardLonger() {
        if (mediaPlayer != null) {
            skipForward(longerSkipTime);
        }
    }
    private void skipForward(Duration interval) {
        Duration duration = mediaPlayer.getCurrentTime();
        duration = duration.add(interval);
        mediaPlayer.seek(duration);
    }
    
    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
    }
}
