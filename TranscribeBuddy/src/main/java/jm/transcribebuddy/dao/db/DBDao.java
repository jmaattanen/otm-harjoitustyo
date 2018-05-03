package jm.transcribebuddy.dao.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDao {
    
    private enum DatabaseType { POSTGRES, SQLITE, UNKNOWN }
    
    protected Connection dbConnection;
    final DatabaseType dbType;
    SQLHelper sqlHelper;
    final protected String databaseURL;
    final protected String databaseUser;
    final protected String databasePass;
    
    
    public DBDao(String sqliteDBPath) {
        if (sqliteDBPath != null && sqliteDBPath.contains(".db")) {
            this.databaseURL = "jdbc:sqlite:" + sqliteDBPath;
            sqlHelper = new SQLiteHelper();
            dbType = DatabaseType.SQLITE;
        } else {
            this.databaseURL = "";
            sqlHelper = null;
            dbType = DatabaseType.UNKNOWN;
        }
        this.databaseUser = "";
        this.databasePass = "";
    }
    
    public DBDao(String databaseURL, String databaseUser, String databasePass) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePass = databasePass;
        if (databaseURL.contains("jdbc:postgresql:")) {
            sqlHelper = new PostgresHelper();
            dbType = DatabaseType.POSTGRES;
        } else if (databaseURL.contains("jdbc:sqlite:")) {
            sqlHelper = new SQLiteHelper();
            dbType = DatabaseType.SQLITE;
        } else {
            sqlHelper = null;
            dbType = DatabaseType.UNKNOWN;
        }
    }
    
    public boolean testConnection() {
        boolean result = connectDatabase();
        closeConnection();
        return result;
    }
    
    final protected boolean connectDatabase() {
        if (dbConnection != null) {
            return true;
        }
        if (sqlHelper == null) {
            return false;
        }
        dbConnection = sqlHelper.connect(databaseURL, databaseUser, databasePass);
        return dbConnection != null;
    }
    
    final protected void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
                dbConnection = null;
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
    
    protected String getCreateProjectsTableQuery() {
        if (sqlHelper == null) {
            return "";
        }
        String sqlQuery = sqlHelper.getCreateProjectsTableQuery();
        return sqlQuery;
    }
    
    protected String getInsertProjectQuery() {
        if (sqlHelper == null) {
            return "";
        }
        String sqlQuery = sqlHelper.getInsertProjectQuery();
        return sqlQuery;
    }
    
    protected String getCreateStatementsTableQuery() {
        if (sqlHelper == null) {
            return "";
        }
        String sqlQuery = sqlHelper.getCreateStatementsTableQuery();
        return sqlQuery;
    }
    
    protected String getInsertStatementQuery() {
        if (sqlHelper == null) {
            return "";
        }
        String sqlQuery = sqlHelper.getInsertStatementQuery();
        return sqlQuery;
    }
    
    protected String getLoadStatementQuery() {
        if (sqlHelper == null) {
            return "";
        }
        String sqlQuery = sqlHelper.getLoadStatementQuery();
        return sqlQuery;
    }
    
}
