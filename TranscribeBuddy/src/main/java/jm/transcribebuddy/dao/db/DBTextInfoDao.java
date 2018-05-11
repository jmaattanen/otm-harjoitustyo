package jm.transcribebuddy.dao.db;

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

/**
 * This DAO class is responsible for storing text information like time marks.
 * 
 * @author juham
 */
public class DBTextInfoDao extends DBDao implements TextInfoDao {
    
    final public static String STATEMENTSTABLE = "tb_statements";
    
    public DBTextInfoDao(String databaseURL, String databaseUser, String databasePass) {
        super(databaseURL, databaseUser, databasePass);
        
        // Create statements table if not exists
        createStatementsTable();
    }
    
    /**
     * Saves text information to database if connection is available.
     * 
     * @param projectInfo This determines to which project the text refers.
     * @param textBuilder The object whose data is stored.
     * @return True if save was successful.
     */
    @Override
    public boolean save(final ProjectInfo projectInfo, final TextBuilder textBuilder) {
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
    
    /**
     * Loads text information from database if connection is available.
     * 
     * @param projectInfo This determines to which project the text refers.
     * @param textBuilder This object will be updated if the load was successful.
     * @return Updated DetailedTextBuilder object if the load was successful.
     * Otherwise the given TextBuilder instance.
     */
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
    
    /**
     * This method removes the project information from database.
     * 
     * @param projectId The identifier that determines the project to be deleted.
     * @return The number of deleted projects.
     */
    public int delete(final int projectId) {
        if (connectDatabase()) {
            int result = deleteAllProjectStatements(projectId);
            closeConnection();
            return result;
        }
        return 0;
    }
    
    private void createStatementsTable() {
        if (connectDatabase() == false) {
            return;
        }
        try {
            String sqlQuery = getCreateStatementsTableQuery();
            PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
            ps.execute();
        } catch (SQLException ex) { 
//            System.out.println("Failed to create table.\n" + ex);
        }
        closeConnection();
    }
    
    private boolean insertStatement(final int projectId, final int statementIndex, Statement statement) {
        if (tableExists(STATEMENTSTABLE)) {
            String sqlQuery = getInsertStatementQuery();
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ps.setInt(2, statementIndex);
                ps.setString(3, statement.toString());
                ps.setDouble(4, statement.startTimeToDouble());
                int result = ps.executeUpdate();
                return result == 1;
            } catch (SQLException ex) {
                System.out.println("insert error:\n" + ex);
            }
        }
        return false;
    }
    
    private int deleteAllProjectStatements(final int projectId) {
        if (tableExists(STATEMENTSTABLE)) {
            String sqlQuery = "DELETE FROM " + STATEMENTSTABLE + " WHERE project_id = ?";
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
        if (tableExists(STATEMENTSTABLE)) {
            String sqlQuery = getLoadStatementQuery();
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
    
}
