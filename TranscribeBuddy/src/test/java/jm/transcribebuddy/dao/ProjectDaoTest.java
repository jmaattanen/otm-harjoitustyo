package jm.transcribebuddy.dao;

import java.io.File;
import java.io.IOException;
import jm.transcribebuddy.logics.MainController;
import jm.transcribebuddy.logics.storage.AppSettings;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class ProjectDaoTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    public File testFolder;
    public String databasePath, textFilePath, sqliteURL;
    
    
    public ProjectDaoTest() {
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
    public void testProjectDao() throws IOException {
        setUpTestConditions();
        assertTrue(testFolder.exists());
        generateAndSaveNewProject();
        testLoadProject();
        assertTrue(testFolder.exists());
    }
    
    
    public void setUpTestConditions() throws IOException {
        testFolder = tempFolder.newFolder("testfolder");
        databasePath = testFolder.getAbsolutePath() + File.separator + "testbase.db";
        textFilePath = testFolder.getAbsolutePath() + File.separator + "testfile.txt";
        sqliteURL = "jdbc:sqlite:" + databasePath;
        sqliteURL = sqliteURL.replaceAll("\\\\", "/");
    }
    
    final String testName = "SQLite Testi";
    final String testDesc = "Testataan pysyväistallennusta.";
    final String testStr1 = "Aloitetaan projektin testaus.";
    final String testStr2 = "Projekti tallennetaan tekstitiedostoon ja tietokantaan.";
    final String testStr3 = "Toivotaan parasta!";
    
    public void generateAndSaveNewProject() {
        AppSettings settings = new AppSettings(sqliteURL, "", "");
        MainController mainController = new MainController(settings);
        mainController.endStatement(testStr1);
        mainController.endStatement(testStr2);
        mainController.set(testStr3);
        ProjectInfo projectInfo = mainController.getProjectInfo();
        projectInfo.setProjectName(testName);
        projectInfo.setDescription(testDesc);
        mainController.setProjectInfo(projectInfo);
        assertEquals(testName, mainController.getProjectName());
        boolean saveOk = mainController.saveProject(textFilePath);
        assertTrue(saveOk);
    }
    
    public void testLoadProject() {
        AppSettings settings = new AppSettings(sqliteURL, "", "");
        MainController mainController = new MainController(settings);
        assertEquals("New Project", mainController.getProjectName());
        boolean loadOk = mainController.loadProject(textFilePath);
        assertTrue(loadOk);
        assertEquals(testName, mainController.getProjectName());
        ProjectInfo projectInfo = mainController.getProjectInfo();
        assertEquals(testDesc, projectInfo.getDescription());
        String lastStatement = mainController.getCurrentStatement();
        assertEquals(testStr3, lastStatement);
        mainController.selectPrevStatement();
        String secondStatement = mainController.getCurrentStatement();
        assertEquals(testStr2, secondStatement);
        mainController.selectPrevStatement();
        String firstStatement = mainController.getCurrentStatement();
        assertEquals(testStr1, firstStatement);
    }
    
}
