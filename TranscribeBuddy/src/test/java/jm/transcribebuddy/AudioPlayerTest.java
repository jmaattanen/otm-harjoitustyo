/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jm.transcribebuddy;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author juham
 */
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
    
    /**
     * Test of getFilePath method, of class AudioPlayer.
     */
//    @Test
//    public void testGetFilePath() {
//        System.out.println("getFilePath");
//        AudioPlayer instance = null;
//        String expResult = "";
//        String result = instance.getFilePath();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of play method, of class AudioPlayer.
     */
//    @Test
//    public void testPlay() {
//        System.out.println("play");
//        AudioPlayer instance = null;
//        instance.play();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of stop method, of class AudioPlayer.
     */
//    @Test
//    public void testStop() {
//        System.out.println("stop");
//        AudioPlayer instance = null;
//        instance.stop();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of skipBackward method, of class AudioPlayer.
     */
//    @Test
//    public void testSkipBackward() {
//        System.out.println("skipBackward");
//        AudioPlayer instance = null;
//        instance.skipBackward();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of skipForward method, of class AudioPlayer.
     */
//    @Test
//    public void testSkipForward() {
//        System.out.println("skipForward");
//        AudioPlayer instance = null;
//        instance.skipForward();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of isPlaying method, of class AudioPlayer.
     */
//    @Test
//    public void testIsPlaying() {
//        System.out.println("isPlaying");
//        AudioPlayer instance = null;
//        boolean expResult = false;
//        boolean result = instance.isPlaying();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
