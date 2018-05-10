package jm.transcribebuddy.dao.db;

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

public class DBProjectInfoDaoTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    public File testFolder, testFile;
    public String databasePath, textFilePath, sqliteURL;
    
    
    public DBProjectInfoDaoTest() {
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
    
    
    public void setUpTestConditions() throws IOException {
        testFolder = tempFolder.newFolder("testfolder");
        testFile = tempFolder.newFile("testfolder/testfile.txt");
        assertTrue(testFolder.exists());
        assertTrue(testFile.exists());
        databasePath = testFolder.getAbsolutePath() + File.separator + "testbase.db";
        textFilePath = testFile.getAbsolutePath();
        sqliteURL = "jdbc:sqlite:" + databasePath;
        sqliteURL = sqliteURL.replaceAll("\\\\", "/");
    }
    
    
    
    @Test
    public void connectDatabaseReturnsFalseIfFails() {
        DBProjectInfoDao fakeDBDao = new DBProjectInfoDao("i", "dont", "exist");
        assertFalse(fakeDBDao.connectDatabase());
        ProjectInfo projectInfo = new ProjectInfo();
        boolean saved = fakeDBDao.save(projectInfo);
        assertFalse(saved);
    }
    
    private void setUpProjectInfo(ProjectInfo projectInfo, String name, String desc) {
        projectInfo.setName(name);
        projectInfo.setDescription(desc);
        projectInfo.setUpFilePaths(textFilePath);
    }
    
    public void testLoading(
            DBProjectInfoDao DBDao,
            final String expectedName,
            final String expectedDesc
    ) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setUpFilePaths(textFilePath);
        projectInfo = DBDao.load(projectInfo);
        assertFalse(projectInfo.isNewProject());
        assertEquals(expectedName, projectInfo.getName());
        assertEquals(expectedDesc, projectInfo.getDescription());
    }
    
    @Test
    public void projectInfoCanBeSavedAndLoaded() throws IOException {
        setUpTestConditions();
        DBProjectInfoDao DBDao = new DBProjectInfoDao(sqliteURL, "", "");
        assertTrue(DBDao.testConnection());
        ProjectInfo projectInfo = new ProjectInfo();
        setUpProjectInfo(projectInfo, "Tärkeä projekti", "Erittäin tärkeä projekti.");
        boolean saved = DBDao.save(projectInfo);
        assertTrue(saved);
        assertEquals("Tärkeä projekti", projectInfo.getName());
        testLoading(DBDao, "Tärkeä projekti", "Erittäin tärkeä projekti.");
    }
    
    @Test
    public void projectInfoCanBeUpdated() throws IOException {
        setUpTestConditions();
        DBProjectInfoDao DBDao = new DBProjectInfoDao(sqliteURL, "", "");
        assertTrue(DBDao.testConnection());
        ProjectInfo projectInfo = new ProjectInfo();
        setUpProjectInfo(projectInfo, "Tärkeä projekti", "Erittäin tärkeä projekti.");
        boolean saved = DBDao.save(projectInfo);
        assertTrue(saved);
        testLoading(DBDao, "Tärkeä projekti", "Erittäin tärkeä projekti.");
        // Set up new information
        projectInfo.setName("Vanhentunut projekti");
        projectInfo.setDescription("Ei enää niin tärkeä projekti.");
        // Update project information to database
        saved = DBDao.save(projectInfo);
        assertTrue(saved);
        testLoading(DBDao, "Vanhentunut projekti", "Ei enää niin tärkeä projekti.");
    }
    
}
