package jm.transcribebuddy.logics;

import java.io.File;
import java.io.IOException;
import jm.transcribebuddy.logics.storage.AppSettings;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class MainControllerTest {
    
    MainController mainController;
    AppSettings settings;
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    public File testFolder;
    public static String databaseURL, liteFolder, sqlURL, textFilePath;
    public static AppSettings sqliteSettings;
    
    
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
    public void setUpSQLiteMadeByHasteProduction() throws IOException {
        testFolder = tempFolder.newFolder("testfolder");
        liteFolder = testFolder.getAbsolutePath();
        System.out.println("lite created: " + liteFolder);
        databaseURL = testFolder.getAbsolutePath() + "\\testbase.db";
        sqlURL = "jdbc:sqlite:" + databaseURL;
        sqlURL = sqlURL.replaceAll("\\\\", "/");
//        System.out.println("testFile:   " + testFile.getAbsolutePath());
//        System.out.println("testFolder: " + testFolder.getAbsolutePath());
//        System.out.println(sqlURL);
        File file = new File(testFolder.getAbsolutePath() + "\\testbase.db");
//        dbDao = new DBDao(sqlURL);
        sqliteSettings = new AppSettings(sqlURL, "", "");
        mainController = new MainController(sqliteSettings);
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProjectName("Lite Test");
        projectInfo.setDescription("SQLiteä tässä testeillaan.");
        mainController.setProjectInfo(projectInfo);
        String name = mainController.getProjectName();
        System.out.println("Desc: " + name);
        System.out.println("File exists: " + file.exists());
        mainController.endStatement("Eka.");
        mainController.set("Toka.");
        textFilePath = liteFolder + "\\testfile.txt";
        System.out.println("TXT file: " + textFilePath);
        boolean saved = mainController.saveProject(textFilePath);
        assertEquals("Lite Test", name);
        assertTrue(saved);
    }
    
//    @Test
//    public void testLoadMadeByHasteProduction() {
//        mainController = new MainController(sqliteSettings);
//        boolean result = mainController.loadProject(textFilePath);
//        System.out.println(sqliteSettings.getDatabaseURL());
////        String name = mainController.getProjectName();
////        assertEquals("Lite Test", name);
//        File file = new File(textFilePath);
//        assertTrue(file.exists());
////        assertTrue(result);
//    }
    
    @Test
    public void testDeleteFile() throws IOException{
        System.out.println("deleting lite: " + liteFolder);
        testFolder = new File(liteFolder);
        testFolder.delete();
        assertFalse(testFolder.exists());
    }
}
