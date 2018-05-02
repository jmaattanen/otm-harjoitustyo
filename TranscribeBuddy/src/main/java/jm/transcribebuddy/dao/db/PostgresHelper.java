package jm.transcribebuddy.dao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresHelper {
    
    public static Connection connectPostgres(String databaseURL, String databaseUser, String databasePass) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(databaseURL, databaseUser, databasePass);
            
        } catch (SQLException | ClassNotFoundException ex) {
//            System.out.println("Failed to connect PostgreSQL\n" + ex);
        }
        
        return connection;
    }
    
    public static String getCreateProjectsTableQuery() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + DBProjectInfoDao.PROJECTSTABLE + " (\n"
                + "id serial UNIQUE, \n"
                + "name varchar(30) NOT NULL, \n"
                + "description varchar(512), \n"
                + "text_file_path varchar(256) NOT NULL, \n"
                + "audio_file_path varchar(256), \n"
                + "PRIMARY KEY(id) \n"
                + ");";
        return sqlQuery;
    }
    
    public static String getInsertProjectQuery() {
        String sqlQuery = "INSERT INTO " + DBProjectInfoDao.PROJECTSTABLE
                + " (name, description, text_file_path, audio_file_path)"
                + "VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
        return sqlQuery;
    }
    
    public static String getCreateStatementsTableQuery() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + DBTextInfoDao.STATEMENTSTABLE + " (\n"
                + "id serial PRIMARY KEY, \n"
                + "project_id serial REFERENCES " + DBProjectInfoDao.PROJECTSTABLE + ", \n"
                + "index integer NOT NULL, \n"
                + "text text NOT NULL, \n"
                + "start_time double precision \n"
                + ");";
        return sqlQuery;
    }
    
    public static String getInsertStatementQuery() {
        String sqlQuery = "INSERT INTO " + DBTextInfoDao.STATEMENTSTABLE
                + " (project_id, index, text, start_time) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT DO NOTHING";
        return sqlQuery;
    }
    
    public static String getLoadStatementQuery() {
        String sqlQuery = "SELECT start_time FROM " + DBTextInfoDao.STATEMENTSTABLE
                + " WHERE project_id = ? AND index = ?";
        return sqlQuery;
    }
    
}
