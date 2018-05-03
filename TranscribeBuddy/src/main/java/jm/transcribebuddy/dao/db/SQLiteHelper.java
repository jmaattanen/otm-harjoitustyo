package jm.transcribebuddy.dao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteHelper implements SQLHelper {
    
    @Override
    public Connection connect(String databaseURL, String databaseUser, String databasePass) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(databaseURL);
        } catch (SQLException | ClassNotFoundException ex) {
//            System.out.println("Failed to connect SQLite\n" + ex);
        }
        return connection;
    }
    
    @Override
    public String getCreateProjectsTableQuery() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + DBProjectInfoDao.PROJECTSTABLE + " (\n"
                    + "id integer UNIQUE, \n"
                    + "name varchar(30) NOT NULL, \n"
                    + "description varchar(512), \n"
                    + "text_file_path varchar(256) NOT NULL, \n"
                    + "audio_file_path varchar(256), \n"
                    + "PRIMARY KEY(id) \n"
                    + ");";
        return sqlQuery;
    }
    
    @Override
    public String getInsertProjectQuery() {
        String sqlQuery = "INSERT INTO " + DBProjectInfoDao.PROJECTSTABLE
                    + " (name, description, text_file_path, audio_file_path)"
                    + "VALUES (?, ?, ?, ?)";
        return sqlQuery;
    }
    
    @Override
    public String getCreateStatementsTableQuery() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + DBTextInfoDao.STATEMENTSTABLE + " (\n"
                + "id integer PRIMARY KEY, \n"
                + "project_id integer REFERENCES " + DBProjectInfoDao.PROJECTSTABLE + "(id), \n"
                + "row integer NOT NULL, \n"
                + "text text NOT NULL, \n"
                + "start_time real \n"
                + ");";
        return sqlQuery;
    }
    
    @Override
    public String getInsertStatementQuery() {
        String sqlQuery = "INSERT INTO " + DBTextInfoDao.STATEMENTSTABLE
                + " (project_id, row, text, start_time) VALUES (?, ?, ?, ?) ";
        return sqlQuery;
    }
    
    @Override
    public String getLoadStatementQuery() {
        String sqlQuery = "SELECT start_time FROM " + DBTextInfoDao.STATEMENTSTABLE
                + " WHERE project_id = ? AND row = ?";
        return sqlQuery;
    }
    
}
