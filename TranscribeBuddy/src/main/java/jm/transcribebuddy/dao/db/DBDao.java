package jm.transcribebuddy.dao.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDao {
    
    private enum DatabaseType { POSTGRES, SQLITE, UNKNOWN }
    
    protected Connection dbConnection;
    final DatabaseType dbType;
    final protected String databaseURL;
    final protected String databaseUser;
    final protected String databasePass;
    
    
    public DBDao(String sqliteDBName) {
        if (sqliteDBName != null && sqliteDBName.contains(".db")) {
            this.databaseURL = "jdbc:sqlite:" + sqliteDBName;
            dbType = DatabaseType.SQLITE;
        } else {
            this.databaseURL = "";
            dbType = DatabaseType.UNKNOWN;
        }
        this.databaseUser = "";
        this.databasePass = "";
    }
    
    public DBDao(String databaseURL, String databaseUser, String databasePass) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePass = databasePass;
        if (databaseURL.contains("jdbc:postgresql")) {
            dbType = DatabaseType.POSTGRES;
        } else if(databaseURL.contains("jdbc:sqlite")) {
            dbType = DatabaseType.SQLITE;
        } else {
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
        switch (dbType) {
            case POSTGRES:
                dbConnection = PostgresHelper.connectPostgres(databaseURL, databaseUser, databasePass);
                break;
            case SQLITE:
                dbConnection = SQLiteHelper.connectSQLite(databaseURL);
                break;
            default:
        }
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
        String sqlQuery;
        switch (dbType) {
            case POSTGRES:
                sqlQuery = PostgresHelper.getCreateProjectsTableQuery();
                break;
            case SQLITE:
                sqlQuery = SQLiteHelper.getCreateProjectsTableQuery();
                break;
            default:
                sqlQuery = "";
        }
        return sqlQuery;
    }
    
    protected String getInsertProjectQuery() {
        String sqlQuery;
        switch (dbType) {
            case POSTGRES:
                sqlQuery = PostgresHelper.getInsertProjectQuery();
                break;
            case SQLITE:
                sqlQuery = SQLiteHelper.getInsertProjectQuery();
                break;
            default:
                sqlQuery = "";
        }
        return sqlQuery;
    }
    
    protected String getCreateStatementsTableQuery() {
        String sqlQuery;
        switch (dbType) {
            case POSTGRES:
                sqlQuery = PostgresHelper.getCreateStatementsTableQuery();
                break;
            case SQLITE:
                sqlQuery = SQLiteHelper.getCreateStatementsTableQuery();
                break;
            default:
                sqlQuery = "";
        }
        return sqlQuery;
    }
    
    protected String getInsertStatementQuery() {
        String sqlQuery;
        switch (dbType) {
            case POSTGRES:
                sqlQuery = PostgresHelper.getInsertStatementQuery();
                break;
            case SQLITE:
                sqlQuery = SQLiteHelper.getInsertStatementQuery();
                break;
            default:
                sqlQuery = "";
        }
        return sqlQuery;
    }
    
    protected String getLoadStatementQuery() {
        String sqlQuery;
        switch (dbType) {
            case POSTGRES:
                sqlQuery = PostgresHelper.getLoadStatementQuery();
                break;
            case SQLITE:
                sqlQuery = SQLiteHelper.getLoadStatementQuery();
                break;
            default:
                sqlQuery = "";
        }
        return sqlQuery;
    }
    
}
