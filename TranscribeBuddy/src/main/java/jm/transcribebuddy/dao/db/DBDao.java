package jm.transcribebuddy.dao.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDao {
    
    protected Connection dbConnection;
    final protected String databaseURL;
    final protected String databaseUser;
    final protected String databasePass;
    
    public DBDao(String databaseURL, String databaseUser, String databasePass) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePass = databasePass;
    }
    
    
    public boolean testConnection() {
        boolean result = connectDatabase();
        closeConnection();
        return result;
    }
    
    final protected boolean connectDatabase() {
        dbConnection = PostgresHelper.connectPostgres(databaseURL, databaseUser, databasePass);
        return dbConnection != null;
    }
    
    final protected void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException ex) { }
        }
    }
    
    protected boolean tableExists(final String tableName) {
        if (dbConnection == null) {
            return false;
        }
        try {
            DatabaseMetaData dbData = dbConnection.getMetaData();
            ResultSet tables = dbData.getTables(null, null, tableName, null);
            if (tables.next()) {
                return true;
            }
        } catch (SQLException ex) { }
        return false;
    }
}
