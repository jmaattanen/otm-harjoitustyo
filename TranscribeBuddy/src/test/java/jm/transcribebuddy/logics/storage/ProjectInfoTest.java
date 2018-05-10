package jm.transcribebuddy.logics.storage;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class ProjectInfoTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    ProjectInfo projectInfo;
    
    public ProjectInfoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        projectInfo = new ProjectInfo();
    }
    
    @After
    public void tearDown() {
    }
    
    
    @Test
    public void setNameWorksRight() {
        final String thirtyChars = "012345678901234567890123456789";
        String name = "";
        for (int i = 0; i < 4; i++) {
            name += thirtyChars;
        }
        projectInfo.setName(name);
        assertEquals(thirtyChars, projectInfo.getName());
    }
    
    @Test
    public void setUpFilePathsWorksRight() throws IOException {
        assertFalse(projectInfo.setUpFilePaths(null));
        File testFolder = tempFolder.newFolder("testfolder");
        final String testFolderPath = testFolder.getAbsolutePath();
        File testFile = tempFolder.newFile("testfolder/myfile.txt");
        final String testFilePath = testFile.getAbsolutePath();
        assertTrue(testFolder.exists());
        assertTrue(testFile.exists());
        assertFalse(projectInfo.setUpFilePaths(testFolderPath));
        projectInfo.setUpFilePaths(testFilePath);
        assertEquals(testFolder.getAbsolutePath(), projectInfo.getSaveDirectory());
        assertEquals(testFilePath, projectInfo.getTextFilePath());
        assertEquals(testFile.getName(), projectInfo.getInitialFileName());
    }
    
}
