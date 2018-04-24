package jm.transcribebuddy.logics;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProjectInfoTest {
    
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
    public void isNewProjectWorks() {
        boolean isNew = projectInfo.isNewProject();
        assertEquals(true, isNew);
    }
}
