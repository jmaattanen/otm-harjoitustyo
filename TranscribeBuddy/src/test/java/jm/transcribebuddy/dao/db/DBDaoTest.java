package jm.transcribebuddy.dao.db;

import java.io.File;
import java.io.IOException;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class DBDaoTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    public File testFolder, testFile;
    
//    DBDao dbDao;
    DBProjectInfoDao projectInfoDao;
    
    
    public DBDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IOException {
        testFolder = tempFolder.newFolder("testfolder");
//        testFile = tempFolder.newFile("testfolder\\testbase.db");
//        final String databaseURL = testFile.getAbsolutePath();
        final String databaseURL = testFolder.getAbsolutePath() + "\\testbase.db";
        String sqlURL = "jdbc:sqlite:" + databaseURL;
//        sqlURL = sqlURL.replaceAll("\\", File.separator);
        sqlURL = sqlURL.replaceAll("\\\\", "/");
//        System.out.println("testFile:   " + testFile.getAbsolutePath());
        System.out.println("testFolder: " + testFolder.getAbsolutePath());
        System.out.println(sqlURL);
        File file = new File(testFolder.getAbsolutePath() + "\\testbase.db");
//        dbDao = new DBDao(sqlURL);
        projectInfoDao = new DBProjectInfoDao(sqlURL, "", "");
        System.out.println("File exists: " + file.exists());
    }
    
    @After
    public void tearDown() {
        if (testFile != null) {
            testFile.delete();
        }
        testFolder.delete();
    }
    
    
    @Test
    public void testCreateFile() throws IOException{
//        System.out.println("testFile:   " + testFile.getAbsolutePath());
//        System.out.println("testFolder: " + testFolder.getAbsolutePath());
//        assertTrue(testFile.exists());
        assertTrue(testFolder.exists());
    }
    
    @Test
    public void connectDatabaseReturnsFalseIfFails() {
        DBDao fakedbDao = new DBDao("i", "dont", "exist");
        assertFalse(fakedbDao.connectDatabase());
    }
    
    @Test
    public void tempSQLiteConnected() {
        boolean connected = projectInfoDao.testConnection();
        assertTrue(connected);
    }
    
    @Test
    public void projectInfoTableExists() {
        System.out.println("pidao null? " + (projectInfoDao == null));
        System.out.println("Table name: " + DBProjectInfoDao.PROJECTSTABLE);
//        boolean tableExists = ((DBDao) projectInfoDao).tableExists(DBProjectInfoDao.PROJECTSTABLE);
        ProjectInfo projectInfo = new ProjectInfo();
        boolean tableExists = projectInfoDao.save(projectInfo);
        assertTrue(tableExists);
    }
    
    
//    @Test
//    public void testDeleteFile() throws IOException{
//        testFile.delete();
//        testFolder.delete();
//        assertFalse(testFile.exists());
//        assertFalse(testFolder.exists());
//    }
    
}
