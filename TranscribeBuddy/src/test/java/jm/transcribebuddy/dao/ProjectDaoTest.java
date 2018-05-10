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
    public void testSavingAndLoading() throws IOException {
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
    
    private void updateProjectInfo(MainController mainCon, String name, String desc) {
        ProjectInfo projectInfo = mainCon.getProjectInfo();
        projectInfo.setName(name);
        projectInfo.setDescription(desc);
        mainCon.setProjectInfo(projectInfo);
    }
    
    public void save(MainController mainController) {
        boolean saveOk = mainController.saveProject(textFilePath);
        assertTrue(saveOk);
        assertFalse(mainController.getProjectInfo().isNewProject());
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
        updateProjectInfo(mainController, testName, testDesc);
        assertEquals(testName, mainController.getProjectName());
        assertTrue(mainController.getProjectInfo().isNewProject());
        save(mainController);
    }
    
    public void testLoadProject() {
        AppSettings settings = new AppSettings(sqliteURL, "", "");
        MainController mainController = new MainController(settings);
        assertEquals("New Project", mainController.getProjectName());
        boolean loadOk = mainController.loadProject(textFilePath);
        assertTrue(loadOk);
        assertFalse(mainController.getProjectInfo().isNewProject());
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
    
    
    @Test
    public void testOverwritingProject() throws IOException {
        setUpTestConditions();
        assertTrue(testFolder.exists());
        AppSettings settings = new AppSettings(sqliteURL, "", "");
        MainController mainController = new MainController(settings);
        updateProjectInfo(mainController, "Ylikirjoitus", "Ylikijoitettava projekti");
        mainController.endStatement("Eka työ.");
        mainController.endStatement("Toka lause.");
        mainController.set("Kolmas lause.");
        // Save first project
        save(mainController);
        // Start to create a new project
        mainController.cleanProject("");
        assertTrue(mainController.getProjectInfo().isNewProject());
        updateProjectInfo(mainController, "Ylikirjoitus", "Korvaava projekti");
        mainController.endStatement("Toka työ.");
        mainController.set("Toisen lauseen korvaava lause.");
        // Save second project and overwrite the first one
        save(mainController);
        // Load project and check that the first project has been overwritten
        mainController = new MainController(settings);
        assertTrue(mainController.getProjectInfo().isNewProject());
        mainController.loadProject(textFilePath);
        assertEquals("Ylikirjoitus", mainController.getProjectName());
        String desc = mainController.getProjectInfo().getDescription();
        assertEquals("Korvaava projekti", desc);
        String firstStatement = mainController.getPrevStatement();
        assertEquals("Toka työ.", firstStatement);
        String secondStatement = mainController.getCurrentStatement();
        assertEquals("Toisen lauseen korvaava lause.", secondStatement);
        // Thirs statement should not exist
        mainController.selectNextStatement();
        String thirdStatement = mainController.getCurrentStatement();
        assertEquals("Toisen lauseen korvaava lause.", thirdStatement);
    }
    
}
