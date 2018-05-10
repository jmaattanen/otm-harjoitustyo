package jm.transcribebuddy.logics;

import jm.transcribebuddy.logics.storage.AppSettings;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainControllerTest {
    
    MainController mainController;
    AppSettings settings;
    
    
    public MainControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        settings = new AppSettings("", "", "");
        mainController = new MainController(settings);
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void workIsSavedAfterInit() {
        boolean isSaved = mainController.isWorkSaved();
        assertEquals(true, isSaved);
    }
    
    @Test
    public void workIsNotSavedAfterEditing() {
        mainController.endStatement("I am hungry.");
        boolean isSaved = mainController.isWorkSaved();
        assertEquals(false, isSaved);
    }
    
    @Test
    public void statementsCanBeSplit() {
        mainController.set("Two words");
        String statement = mainController.getCurrentStatement();
        mainController.splitStatement(statement, 3);
        statement = mainController.getCurrentStatement();
        assertEquals("words", statement);
    }
    
    @Test
    public void statementIsNotSplitFromZeroIndex() {
        final String expectedStatement = "Please don't split me!";
        mainController.set(expectedStatement);
        mainController.splitStatement(expectedStatement, 0);
        mainController.selectPrevStatement();
        String statement = mainController.getCurrentStatement();
        assertEquals(expectedStatement, statement);
    }
    
    @Test
    public void selectByCaretPositionFindsFirstStatement() {
        mainController.endStatement("Eka lause.");
        mainController.endStatement("Toka lause.");
        mainController.endStatement("Kolmas lause.");
        // caret is now positioned in the beginning of text
        int caretPos = 0;
        mainController.selectStatementByCaretPosition(caretPos);
        String statement = mainController.getCurrentStatement();
        assertEquals("Eka lause.", statement);
    }
    
    @Test
    public void selectByCaretPositionFindsSecondStatement() {
        final String firstStatement = "Eka lause.";
        final String secondStatement = "Toka lause.";
        mainController.endStatement(firstStatement);
        mainController.endStatement(secondStatement);
        mainController.set("Kolmas lause.");
        int caretPos = firstStatement.length() + 2;
        mainController.selectStatementByCaretPosition(caretPos);
        String statement = mainController.getCurrentStatement();
        assertEquals(secondStatement, statement);
    }
    
    @Test
    public void parseLastStatementRetainsTextState() {
        mainController.endStatement("Testi lause.");
        mainController.endStatement("Toinen testi lause.");
        mainController.set("Kolmas testi lause.");
        final String statementBefore = mainController.getCurrentStatement();
        final String text = mainController.getFullText();
        mainController.parseLastStatement(text);
        String statementAfter = mainController.getCurrentStatement();
        assertEquals(statementBefore, statementAfter);
    }
    
    @Test
    public void parseLastStatementDetectsModifications() {
        mainController.endStatement("Testi lause.");
        mainController.endStatement("Toinen testi lause.");
        mainController.set("Kolmas testi.");
        String text = mainController.getFullText();
        final String replacement = "Kolmas testi lause.";
        text = text.replaceFirst("Kolmas testi.", replacement);
        mainController.parseLastStatement(text);
        String statement = mainController.getCurrentStatement();
        assertEquals(replacement, statement);
    }
    
    @Test
    public void subcategoryCanBeSet() {
        mainController.set("Uusi lause.");
        // foo category won't affect
        mainController.setSubcategory("");
        String subcategory = mainController.getCurrentSubcategory();
        assertEquals("Undefined", subcategory);
        // this should be valid category name
        mainController.setSubcategory("Testiryhmä");
        subcategory = mainController.getCurrentSubcategory();
        assertEquals("Testiryhmä", subcategory);
    }
    
    @Test
    public void timeMarksCanBeSet() {
        MockPlayer mockplayer = new MockPlayer();
        mainController = new MainController(settings, mockplayer);
        assertEquals("No audio", mainController.getAudioFileURI());
        mainController.openAudioFile("file:mock.mp3");
        assertEquals("file:mock.mp3", mainController.getAudioFileURI());
        mainController.playAudio();
        mainController.endStatement("Eka lause.");
        mainController.endStatement("Toka lause.");
        mainController.set("Kolmas lause.");
        mainController.stopAudio();
        String mark3 = mainController.startTimeToString();
        mainController.selectPrevStatement();
        String mark2 = mainController.startTimeToString();
        mainController.selectPrevStatement();
        String mark1 = mainController.startTimeToString();
        assertEquals("00:00", mark1);
        assertEquals("00:01", mark2);
        assertEquals("00:02", mark3);
        mainController.selectNextStatement();
        mainController.selectNextStatement();
        mainController.seekBeginningOfCurrentStatement();
        mainController.skipForward();
        mainController.setStartTime();
        mark3 = mainController.startTimeToString();
        assertEquals("00:07", mark3);
    }
    
}
