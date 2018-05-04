package jm.transcribebuddy.logics;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AudioPlayerTest {
    
    public AudioPlayerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void incorrectParameterInitializesFilePathRight() {
        AudioPlayer audioPlayer = new AudioPlayer(null);
        assertEquals("No audio", audioPlayer.getFilePath());
    }
    
    @Test
    public void fooOpensWontThrowExceptions() {
        AudioPlayer audioPlayer = new AudioPlayer(null);
        audioPlayer.openAudioFile(null);
        assertFalse(audioPlayer.isSet());
        audioPlayer.openAudioFile("");
        assertFalse(audioPlayer.isSet());
        audioPlayer.openAudioFile("file:/player/foo.mp3");
        assertFalse(audioPlayer.isSet());
    }
}
