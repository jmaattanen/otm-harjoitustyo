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
        assertEquals("Ei audiota", audioPlayer.getFilePath());
    }
    
    @Test
    public void openMethodCanBeCalledWithEmptyString() {
        AudioPlayer audioPlayer = new AudioPlayer(null);
        audioPlayer.openAudioFile("");
        assertEquals(false, audioPlayer.isSet());
    }
}
