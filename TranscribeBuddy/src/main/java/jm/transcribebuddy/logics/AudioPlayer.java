package jm.transcribebuddy.logics;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer {
    
    private String audioFilePath;
    private MediaPlayer mediaPlayer;
    private Duration jumpTime;
    
    public AudioPlayer() {
        jumpTime = Duration.seconds(5);
        audioFilePath = "Ei audiota";
    }
    
    public AudioPlayer(String filePath) {
        if (filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            audioFilePath = filePath;
            jumpTime = Duration.seconds(5);
        } else {
            audioFilePath = "Ei audiota";
        }
    }
    
    public void openAudioFile(String filePath) {
        this.stop();
        if (filePath != null) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            audioFilePath = filePath;
            jumpTime = Duration.seconds(5);
        }
    }
    
    public String getFilePath() {
        return audioFilePath;
    }
    
    public Duration getCurrentTime() {
        if (mediaPlayer == null) {
            return Duration.seconds(0);
        } else return mediaPlayer.getCurrentTime();
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
            if( isPlaying() )
                mediaPlayer.pause();
            else mediaPlayer.play();
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
            Duration duration = mediaPlayer.getCurrentTime();
            if (duration.greaterThan(jumpTime)) {
                duration = duration.subtract(jumpTime);
            } else {
                duration = mediaPlayer.getStartTime();
            }
            mediaPlayer.seek(duration);
        }
    }
    
    public void skipForward() {
        if (mediaPlayer != null) {
            Duration duration = mediaPlayer.getCurrentTime();
            duration = duration.add(jumpTime);
            mediaPlayer.seek(duration);
        }
    }
    
    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
    }
}
