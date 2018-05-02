package jm.transcribebuddy.dao.db;

/***   This is DAO that is responsible for storing text info like time marks    ***/

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

public class DBTextInfoDao extends DBDao implements TextInfoDao {
    
    final public static String STATEMENTSTABLE = "tb_statements";
    
    public DBTextInfoDao(String databaseURL, String databaseUser, String databasePass) {
        super(databaseURL, databaseUser, databasePass);
        
        // Create statements table if not exists
        createStatementsTable();
    }
    
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
