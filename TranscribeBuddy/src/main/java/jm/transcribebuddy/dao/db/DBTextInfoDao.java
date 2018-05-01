package jm.transcribebuddy.dao.db;

/***   This is DAO that is responsible for storing text info like time marks    ***/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jm.transcribebuddy.dao.TextInfoDao;
import jm.transcribebuddy.logics.word.DetailedTextBuilder;
import jm.transcribebuddy.logics.word.DuctileTextBuilder;
import jm.transcribebuddy.logics.storage.ProjectInfo;
import jm.transcribebuddy.logics.storage.Statement;
import jm.transcribebuddy.logics.word.TextBuilder;

public class DBTextInfoDao implements TextInfoDao {
    
    private Connection dbConnection;
    final private String databaseURL;
    final private String databaseUser;
    final private String databasePass;
    
    final private String dbTableNameForStatements = "tb_statements";
    final private int maxStatementLength = 1024;
    
    public DBTextInfoDao(String databaseURL, String databaseUser, String databasePass) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePass = databasePass;
        
        // Create statements table if not exists
        if (connectDatabase()) {
            createStatementsTable();
            closeConnection();
        }
    }
    
    @Override
    public boolean save(final ProjectInfo projectInfo, TextBuilder textBuilder) {
        if (connectDatabase() == false) {
            return false;
        }
        final int projectId = projectInfo.getId();
        
        // Delete old text info of the project
        deleteAllProjectStatements(projectId);
        
        boolean result = true;
        ArrayList<Statement> statements = textBuilder.getAllStatements();
        int statementId = 1;
        for (Statement s : statements) {
            if (insertStatement(projectId, statementId, s) == false) {
                // insert failed
                result = false;
            }
            statementId++;
        }
        closeConnection();
        return result;
    }
    
    @Override
    public DetailedTextBuilder load(final ProjectInfo projectInfo, final DetailedTextBuilder textBuilder) {
        if (connectDatabase() == false) {
            return textBuilder;
        }
        final int projectId = projectInfo.getId();
        
        // Construct a new TextBuilder with loaded text info
        DuctileTextBuilder updatedTextBuilder = new DuctileTextBuilder();

        ArrayList<Statement> statements = textBuilder.getAllStatements();
        int statementIndex = 1;
        for (Statement s : statements) {
            Statement statement = loadStatement(projectId, statementIndex, s);
            updatedTextBuilder.addNewStatement(statement);
            statementIndex++;
        }
        closeConnection();
        if (updatedTextBuilder.isValid()) {
            // load ok
            return updatedTextBuilder;
        }
        return textBuilder;
    }
    
    public int delete(final int projectId) {
        if (connectDatabase()) {
            int result = deleteAllProjectStatements(projectId);
            closeConnection();
            return result;
        }
        return 0;
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
            } catch (SQLException ex) { }
        }
    }
    
    private boolean createStatementsTable() {
        if (dbConnection == null) {
            return false;
        }
        try {
            String qs = "CREATE TABLE IF NOT EXISTS " + dbTableNameForStatements + " (\n"
                    + "id serial PRIMARY KEY, \n"
                    + "project_id serial REFERENCES tb_projects, \n"
                    + "index integer NOT NULL, \n"
                    + "text varchar(" + maxStatementLength + ") NOT NULL, \n"
                    + "start_time double precision \n"
                    + ");";
            PreparedStatement ps = dbConnection.prepareStatement(qs);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            System.out.println("Failed to create " + dbTableNameForStatements + " table\n" + ex);
        }
        return false;
    }
    
    private boolean insertStatement(final int projectId, final int statementIndex, Statement statement) {
        if (tableExists(dbTableNameForStatements)) {
            String sqlQuery = "INSERT INTO " + dbTableNameForStatements
                    + " (project_id, index, text, start_time) VALUES (?, ?, ?, ?) "
                    + "ON CONFLICT DO NOTHING";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ps.setInt(2, statementIndex);
                String text = fitTextForDB(statement.toString());
                ps.setString(3, text);
                ps.setDouble(4, statement.startTimeToDouble());
                int result = ps.executeUpdate();
                return result == 1;
            } catch (SQLException ex) { }
        }
        return false;
    }
    
    private String fitTextForDB(String text) {
        if (text.length() > maxStatementLength) {
            return text.substring(0, maxStatementLength);
        }
        return text;
    }
    
    private int deleteAllProjectStatements(final int projectId) {
        if (tableExists(dbTableNameForStatements)) {
            String sqlQuery = "DELETE FROM " + dbTableNameForStatements + " WHERE project_id = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                int result = ps.executeUpdate();
                return result;
            } catch (SQLException ex) {
//                System.out.println("Failed to delete from " + dbTableNameForStatements + "\n" + ex);
            }
        }
        return 0;
    }
    
    private Statement loadStatement(final int projectId, final int statementIndex, Statement statement) {
        if (tableExists(dbTableNameForStatements)) {
            String sqlQuery = "SELECT start_time FROM " + dbTableNameForStatements + " WHERE project_id = ? AND index = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ps.setInt(2, statementIndex);
                ResultSet results = ps.executeQuery();
                if (results.next()) {
                    double startTimeInMillis = results.getDouble(1);
                    statement.setStartTime(startTimeInMillis);
                }
            } catch (SQLException ex) {
//                System.out.println("Failed to select from " + dbTableNameForStatements + "\n" + ex);
            }
        }
        return statement;
    }
    
    private boolean tableExists(final String tableName) {
        return DBHelper.tableExists(tableName, dbConnection);
    }
}
