package jm.transcribebuddy.dao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresHelper implements SQLHelper {
    
    @Override
    public Connection connect(String databaseURL, String databaseUser, String databasePass) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(databaseURL, databaseUser, databasePass);
            
        } catch (SQLException | ClassNotFoundException ex) {
//            System.out.println("Failed to connect PostgreSQL\n" + ex);
        }
        
        return connection;
    }
    
    @Override
    public  String getCreateProjectsTableQuery() {
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
    
    @Override
    public String getInsertProjectQuery() {
        String sqlQuery = "INSERT INTO " + DBProjectInfoDao.PROJECTSTABLE
                + " (name, description, text_file_path, audio_file_path)"
                + "VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
        return sqlQuery;
    }
    
    @Override
    public String getCreateCategoriesTableQuery() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + DBClassifierDao.CATEGORIESTABLE + " (\n"
                + "id serial PRIMARY KEY, \n"
                + "project_id serial REFERENCES " + DBProjectInfoDao.PROJECTSTABLE + ", \n"
//                + "parent_id serial REFERENCES " + DBClassifierDao.CATEGORIESTABLE + ", \n"
                + "parent_id integer, \n"
                + "name text NOT NULL, \n"
                + "depth integer NOT NULL \n"
                + ");";
        return sqlQuery;
    }
    
    @Override
    public String getInsertCategoryQuery() {
        String sqlQuery = "INSERT INTO " + DBClassifierDao.CATEGORIESTABLE
                + " (project_id, parent_id, name, depth) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT DO NOTHING";
        return sqlQuery;
    }
    
    
    @Override
    public String getCreateStatementsTableQuery() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + DBTextInfoDao.STATEMENTSTABLE + " (\n"
                + "id serial PRIMARY KEY, \n"
                + "project_id serial REFERENCES " + DBProjectInfoDao.PROJECTSTABLE + ", \n"
                + "index integer NOT NULL, \n"
                + "text text NOT NULL, \n"
                + "start_time double precision \n"
                + ");";
        return sqlQuery;
    }
    
    @Override
    public String getInsertStatementQuery() {
        String sqlQuery = "INSERT INTO " + DBTextInfoDao.STATEMENTSTABLE
                + " (project_id, index, text, start_time) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT DO NOTHING";
        return sqlQuery;
    }
    
    @Override
    public String getLoadStatementQuery() {
        String sqlQuery = "SELECT start_time FROM " + DBTextInfoDao.STATEMENTSTABLE
                + " WHERE project_id = ? AND index = ?";
        return sqlQuery;
    }
    
    @Override
    public String getStatementIdQuery() {
        String sqlQuery = "SELECT id FROM " + DBTextInfoDao.STATEMENTSTABLE
                    + " WHERE project_id = ? AND index = ?";
        return sqlQuery;
    }
    
    @Override
    public String getCreateCatsAndStatesTableQuery() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + DBClassifierDao.CATSANDSTATESTABLE + " (\n"
//                + "id serial PRIMARY KEY, \n"
                + "project_id serial REFERENCES " + DBProjectInfoDao.PROJECTSTABLE + ", \n"
                + "category_id serial REFERENCES " + DBClassifierDao.CATEGORIESTABLE + ", \n"
                + "statement_id serial REFERENCES " + DBTextInfoDao.STATEMENTSTABLE + " \n"
                + ");";
        return sqlQuery;
    }
    
    @Override
    public String getInsertIntoCatsAndStatesQuery() {
        String sqlQuery = "INSERT INTO " + DBClassifierDao.CATSANDSTATESTABLE
                + " (project_id, category_id, statement_id) VALUES (?, ?, ?) "
                + "ON CONFLICT DO NOTHING";
        return sqlQuery;
    }
    
}
