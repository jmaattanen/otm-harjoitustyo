package jm.transcribebuddy.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import jm.transcribebuddy.logics.TextBuilder;

public class DBTextInfoDao implements TextInfoDao {
    
    private Connection dbConnection;
    private String databaseUrl;
    private String databaseUser;
    private String databasePass;
    
    public DBTextInfoDao() {
        databaseUrl = "jdbc:postgresql://localhost:5432/mytestdb";
        databaseUser = "postgres";
        databasePass = "mayipass";
    }
    
    @Override
    public void save(TextBuilder textBuilder) {
        
    }
    
    public boolean testConnection() {
        boolean result = connectDatabase();
        closeConnection();
        return result;
    }
    
    private boolean connectDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            dbConnection = DriverManager.getConnection(databaseUrl, databaseUser, databasePass);
            return true;
        } catch (SQLException ex) {
            System.out.println("Failed to connect database\n" + ex);
        }
        catch (ClassNotFoundException e) {
            e.getMessage();
        }
        return false;
    }
    
    private void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException ex) {
                System.out.println("Failed to close database connection\n" + ex);
            }
        }
    }

}
