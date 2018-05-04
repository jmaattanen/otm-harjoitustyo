package jm.transcribebuddy.logics;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer {
    
    private String audioFilePath;
    private MediaPlayer mediaPlayer;
    private Duration skipTime, longerSkipTime;
    
    public AudioPlayer() {
        audioFilePath = "No audio";
        skipTime = Duration.seconds(5);
        longerSkipTime = Duration.seconds(30);
    }
    
    public AudioPlayer(String audioFileURI) {
        this();
        mediaPlayer = null;
        openAudioFile(audioFileURI);
    }
    
    public boolean isSet() {
        return mediaPlayer != null;
    }
    
    public final void openAudioFile(String audioFileURI) {
        if (isSupportedURI(audioFileURI) == false) {
            return;
        }
        this.stop();
        URI uri;
        try {
            uri = new URI(audioFileURI);
        } catch (URISyntaxException e) {
            return;
        }
        File audioFile = new File(uri);
        if (audioFile.exists()) {
            Media media = new Media(audioFileURI);
            mediaPlayer = new MediaPlayer(media);
            this.audioFilePath = audioFileURI;
        }
    }
    
    public static boolean isSupportedURI(String audioFileURI) {
        if (audioFileURI == null) {
            return false;
        }
        if (audioFileURI.contains("file:") == false) {
            return false;
        }
        if (audioFileURI.contains(".wav") || audioFileURI.contains(".mp3") | audioFileURI.contains(".m4a")) {
            return true;
        }
        return false;
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
    
    public void seekTimeMark(Duration timeMark) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(timeMark);
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
