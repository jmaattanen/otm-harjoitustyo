package jm.transcribebuddy.dao.db;

/***   DAO for storing project information like project name and resource paths    ***/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jm.transcribebuddy.dao.ProjectInfoDao;
import jm.transcribebuddy.logics.storage.ProjectInfo;

public class DBProjectInfoDao implements ProjectInfoDao {
    
    private Connection dbConnection;
    final private String databaseURL;
    final private String databaseUser;
    final private String databasePass;
    
    final private String dbTableNameForProjects = "tb_projects";
    
    public DBProjectInfoDao(String databaseURL, String databaseUser, String databasePass) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePass = databasePass;
        
        // Create statements table if not exists
        if (connectDatabase()) {
            createProjectsTable();
            closeConnection();
        }
    }
    
    @Override
    public boolean save(final ProjectInfo projectInfo) {
        if (connectDatabase() == false) {
            return false;
        }
        boolean result;
        final int projectId = projectInfo.getId();
        final String textFilePath = projectInfo.getTextFilePath();
        if (projectExists(projectId) && hasSameTextFilePath(projectId, textFilePath)) {
            // Project exists already and text file path has not changed
            // so update instead of creating new row
            result = updateProjectInfo(projectInfo);
        } else {
            // Create a new project with a new id
            // and delete other projects that are using this text file
            deleteProjectsThatUse(textFilePath);
            result = insertProjectInfo(projectInfo);
        }
        closeConnection();
        return result;
    }
    
    @Override
    public ProjectInfo load(ProjectInfo projectInfo) {
        if (connectDatabase() == true) {
            projectInfo = loadProjectInfo(projectInfo);
            closeConnection();
        }
        return projectInfo;
    }
    
    public boolean testConnection() {
        boolean result = connectDatabase();
        closeConnection();
        return result;
    }
    
    private boolean connectDatabase() {
        dbConnection = DBHelper.connectPostgres(databaseURL, databaseUser, databasePass);
        return dbConnection != null;
    }
    
    private void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException ex) {
//                System.out.println("Failed to close database connection\n" + ex);
            }
        }
    }
    
    private boolean createProjectsTable() {
        if (dbConnection == null) {
            return false;
        }
        try {
            String qs = "CREATE TABLE IF NOT EXISTS " + dbTableNameForProjects + " (\n"
                    + "id serial UNIQUE, \n"
                    + "name varchar(30) NOT NULL, \n"
                    + "description varchar(512), \n"
                    + "text_file_path varchar(256) NOT NULL, \n"
                    + "audio_file_path varchar(256), \n"
                    + "PRIMARY KEY(id) \n"
                    + ");";
            PreparedStatement ps = dbConnection.prepareStatement(qs);
            ps.execute();
            return true;
        } catch (SQLException ex) {
//            System.out.println("Failed to create " + dbTableNameForStatements + " table\n" + ex);
        }
        return false;
    }
    
    private boolean insertProjectInfo(final ProjectInfo projectInfo) {
        if (tableExists(dbTableNameForProjects)) {
            String sqlQuery = "INSERT INTO " + dbTableNameForProjects
                    + " (name, description, text_file_path, audio_file_path)"
                    + "VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                final String textFilePath = projectInfo.getTextFilePath();
                ps.setString(1, projectInfo.getName());
                ps.setString(2, projectInfo.getDescription());
                ps.setString(3, textFilePath);
                ps.setString(4, projectInfo.getAudioFilePath());
                int result = ps.executeUpdate();
                // update project id
                final int projectId = getProjectId(textFilePath);
                projectInfo.setId(projectId);
                return result == 1;
            } catch (SQLException ex) { }
        }
        return false;
    }
    
    private boolean updateProjectInfo(final ProjectInfo projectInfo) {
        if (tableExists(dbTableNameForProjects)) {
            String sqlQuery = "UPDATE " + dbTableNameForProjects + " SET "
                    + "name = ?, description = ?, "
                    + "text_file_path = ?, audio_file_path = ? "
                    + "WHERE id = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setString(1, projectInfo.getName());
                ps.setString(2, projectInfo.getDescription());
                ps.setString(3, projectInfo.getTextFilePath());
                ps.setString(4, projectInfo.getAudioFilePath());
                ps.setInt(5, projectInfo.getId());
                int result = ps.executeUpdate();
                return result == 1;
            } catch (SQLException ex) { }
        }
        return false;
    }
    
    private int deleteProject(final int projectId) {
        if (tableExists(dbTableNameForProjects)) {
            // Delete all statements first
            DBTextInfoDao textInfoDao = new DBTextInfoDao(databaseURL, databaseUser, databasePass);
            textInfoDao.delete(projectId);
            // Then delete project information
            String sqlQuery = "DELETE FROM " + dbTableNameForProjects + " WHERE id = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                int result = ps.executeUpdate();
                return result;
            } catch (SQLException ex) { }
        }
        return 0;
    }
    
    private void deleteProjectsThatUse(final String textFilePath) {
        if (tableExists(dbTableNameForProjects)) {
            String sqlQuery = "SELECT  id FROM " + dbTableNameForProjects
                    + " WHERE text_file_path = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setString(1, textFilePath);
                ResultSet results = ps.executeQuery();
                while (results.next()) {
                    int projectId = results.getInt(1);
                    deleteProject(projectId);
                }
            } catch (SQLException ex) { }
        }
    }
    
    private ProjectInfo loadProjectInfo(final ProjectInfo projectInfo) {
        if (tableExists(dbTableNameForProjects)) {
            String sqlQuery = "SELECT id, name, description, audio_file_path"
                    + " FROM " + dbTableNameForProjects + " WHERE text_file_path = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setString(1, projectInfo.getTextFilePath());
                ResultSet results = ps.executeQuery();
                if (results.next()) {
                    projectInfo.setId(results.getInt(1));
                    projectInfo.setProjectName(results.getString(2));
                    projectInfo.setDescription(results.getString(3));
                    projectInfo.setAudioFilePath(results.getString(4));
                }
            } catch (SQLException ex) { }
        }
        return projectInfo;
    }
    
    private boolean projectExists(final int projectId) {
        if (tableExists(dbTableNameForProjects)) {
            String sqlQuery = "SELECT  name FROM " + dbTableNameForProjects
                    + " WHERE id = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ResultSet results = ps.executeQuery();
                if (results.next()) {
                    return true;
                }
            } catch (SQLException ex) { }
        }
        return false;
    }
    
    private boolean hasSameTextFilePath(final int projectId, final String textFilePath) {
        if (tableExists(dbTableNameForProjects)) {
            String sqlQuery = "SELECT  text_file_path FROM " + dbTableNameForProjects
                    + " WHERE id = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ResultSet results = ps.executeQuery();
                if (results.next()) {
                    String oldPath = results.getString(1);
                    return textFilePath.equals(oldPath);
                }
            } catch (SQLException ex) { }
        }
        return true;
    }
    
    private int getProjectId(final String textFilePath) {
        if (tableExists(dbTableNameForProjects)) {
            String sqlQuery = "SELECT  id FROM " + dbTableNameForProjects
                    + " WHERE text_file_path = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setString(1, textFilePath);
                ResultSet results = ps.executeQuery();
                if (results.next()) {
                    return results.getInt(1);
                }
            } catch (SQLException ex) { }
        }
        return 0;
    }
    
    private boolean tableExists(final String tableName) {
        return DBHelper.tableExists(tableName, dbConnection);
    }
}
