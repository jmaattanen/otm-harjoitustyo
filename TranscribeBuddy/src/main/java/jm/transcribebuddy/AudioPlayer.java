package jm.transcribebuddy;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer {
    
    final private String audioFilePath;
    private MediaPlayer mediaPlayer;
    private Duration duration;
    private Duration jumpTime;
    
    public AudioPlayer(String filePath) {
        if(filePath != null ) {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            audioFilePath = filePath;
            duration = mediaPlayer.getCurrentTime();
            jumpTime = Duration.seconds(5);
        }
        else audioFilePath = "Ei audiota";
    }
    
    public String getFilePath() {
        return audioFilePath;
    }
    
    public void play() {
        mediaPlayer.play();
    }
    
    public void stop() {
        mediaPlayer.pause();
        duration = mediaPlayer.getCurrentTime();
    }
    
    public void skipBackward() {
        duration = mediaPlayer.getCurrentTime();
        if( duration.greaterThan(jumpTime))
            duration = duration.subtract(jumpTime);
        else duration = mediaPlayer.getStartTime();
        mediaPlayer.seek(duration);
    }
    
    public void skipForward() {
        duration = mediaPlayer.getCurrentTime();
        duration = duration.add(jumpTime);
        mediaPlayer.seek(duration);
    }
    
    public boolean isPlaying() {
        if( mediaPlayer == null )
            return false;
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
    }
}
