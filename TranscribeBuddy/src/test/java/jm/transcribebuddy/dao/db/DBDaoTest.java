package jm.transcribebuddy.dao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

public class DBDaoTest {
    
    static Connection connection;
    
    public DBDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:testdata/database.db");
        } catch (SQLException ex) {
            Logger.getLogger(DBDaoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        DBDao dbDao = new DBDao("i", "dont", "exist");
        assertFalse(dbDao.connectDatabase());
    }
    
    @Test
    public void sqliteConnected() {
        assertNotNull(connection);
    }
    
    @Test
    public void sqliteCanBeConnected() {
//        DBDao dbDao = new DBDao("testLiteBase.db");
        DBDao dbDao = new DBDao("testdata/testLiteBase.db");
        boolean connected = dbDao.connectDatabase();
        dbDao.closeConnection();
        assertTrue(connected);
    }
}
