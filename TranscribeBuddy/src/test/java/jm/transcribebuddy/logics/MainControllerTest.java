package jm.transcribebuddy.logics;

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
}
