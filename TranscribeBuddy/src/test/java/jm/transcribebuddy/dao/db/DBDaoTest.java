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

public class DBDaoTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    public File testFolder;
    public String databasePath, textFilePath, sqliteURL;
    
    
    public DBDaoTest() {
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
    public void connectDatabaseReturnsFalseIfFails() {
        DBDao fakedbDao = new DBDao("i", "dont", "exist");
        assertFalse(fakedbDao.connectDatabase());
    }
    
    
}
