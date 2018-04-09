package jm.transcribebuddy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.scene.media.AudioClip;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
    
    final private String audioFilePath;
    final private AudioClip audioClip;
    
    public AudioPlayer(String filePath) {
        /*
        File audioFile = new File(filePath);
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream( audioFile.toURI().toURL() );
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            //clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }//*/
        /*
        final URL resource = getClass().getResource(filePath);//"Recording.m4a");
        if( resource != null ) {
            audioFilePath = filePath;
            //audioClip = new AudioClip(resource.toExternalForm());
        }//*/
        audioClip = new AudioClip(filePath);
        if( audioClip == null )
            audioFilePath = "Ei audiota";
        else audioFilePath = filePath;
    }
    
    public String getFilePath() {
        return audioFilePath;
    }
    
    public void play() {
        if(audioClip != null) {
            audioClip.stop();
            audioClip.play();
        }
    }
    
    public void stop() {
        audioClip.stop();
    }
    
    public boolean isPlaying() {
        return audioClip.isPlaying();
    }
}
