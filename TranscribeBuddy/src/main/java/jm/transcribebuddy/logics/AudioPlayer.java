package jm.transcribebuddy.logics;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer implements Player {
    
    private String audioFileURI;
    private MediaPlayer mediaPlayer;
    private Duration skipTime, longerSkipTime;
    
    public AudioPlayer() {
        audioFileURI = "No audio";
        skipTime = Duration.seconds(5);
        longerSkipTime = Duration.seconds(30);
    }
    
    public AudioPlayer(String audioFileURI) {
        this();
        mediaPlayer = null;
        openAudioFile(audioFileURI);
    }
    
    @Override
    public boolean isSet() {
        return mediaPlayer != null;
    }
    
    @Override
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
            this.audioFileURI = audioFileURI;
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
    
    @Override
    public String getFileURI() {
        return audioFileURI;
    }
    
    @Override
    public Duration getCurrentTime() {
        if (mediaPlayer == null) {
            return Duration.seconds(0);
        }
        return mediaPlayer.getCurrentTime();
    }
    
    @Override
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
    
    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    
    @Override
    public void changePlayingStatus() {
        if (mediaPlayer != null) {
            if (isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }
    
    @Override
    public void seekBeginning() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(0));
        }
    }
    
    @Override
    public void seekTimeMark(Duration timeMark) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(timeMark);
        }
    }
    
    @Override
    public void skipBackward() {
        if (mediaPlayer != null) {
            skipBackward(skipTime);
        }
    }
    @Override
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
    
    @Override
    public void skipForward() {
        if (mediaPlayer != null) {
            skipForward(skipTime);
        }
    }
    @Override
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
    
    @Override
    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
    }
}
