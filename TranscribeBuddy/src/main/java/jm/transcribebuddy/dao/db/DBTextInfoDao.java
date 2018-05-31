package jm.transcribebuddy.dao.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jm.transcribebuddy.dao.TextInfoDao;
import jm.transcribebuddy.logics.storage.LeafCategory;
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
        
        // Delete old project data
        deleteAllCatsAndStatements(projectId);
        deleteAllProjectStatements(projectId);
        
        boolean result = true;
        ArrayList<Statement> statements = textBuilder.getAllStatements();
        int statementIndex = 1;
        for (Statement s : statements) {
            if (insertStatement(projectId, statementIndex, s) == false) {
                // insert failed
                result = false;
            }
            statementIndex++;
        }
        closeConnection();
        return result;
    }
    
    /**
     * Loads text information from database if connection is available.
     * 
     * @param projectId This determines to which project the text refers.
     * @param textBuilder This object will be updated if the load was successful.
     * @return Updated DetailedTextBuilder object if the load was successful.
     * Otherwise the given TextBuilder instance.
     */
    @Override
    public DetailedTextBuilder load(final int projectId, final DetailedTextBuilder textBuilder) {
        if (connectDatabase() == false) {
            return textBuilder;
        }
        
        // Construct a new TextBuilder with loaded text info
        DuctileTextBuilder updatedTextBuilder = new DuctileTextBuilder();

        ArrayList<Statement> statements = textBuilder.getAllStatements();
        int statementIndex = 1;
        for (Statement s : statements) {
            Statement statement = loadStatement(projectId, statementIndex, s);
            String categoryName = getSubcategory(projectId, statementIndex);
            updatedTextBuilder.addNewStatement(statement, categoryName);
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
            int result = 0;
            result += deleteAllCatsAndStatements(projectId);
            result += deleteAllProjectStatements(projectId);
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
                if (result == 1) {
                    insertIntoCatsAndStates(projectId, statementIndex, statement.getSubcategory());
                }
                return result == 1;
            } catch (SQLException ex) {
//                System.out.println("insert error:\n" + ex);
            }
        }
        return false;
    }
    
    private boolean insertIntoCatsAndStates(final int projectId, int statementIndex, LeafCategory subcategory) {
        if (subcategory == null || tableExists(DBClassifierDao.CATSANDSTATESTABLE) == false) {
            return false;
        }
        final int statementId = getStatementId(projectId, statementIndex);
        final int categoryId = getCategoryId(projectId, subcategory);
        if (statementId == 0 || categoryId == 0) {
            return false;
        }
        String sqlQuery = "INSERT INTO " + DBClassifierDao.CATSANDSTATESTABLE
                + " (project_id, category_id, statement_id) VALUES (?, ?, ?) "
                + "ON CONFLICT DO NOTHING";
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
            ps.setInt(1, projectId);
            ps.setInt(2, categoryId);
            ps.setInt(3, statementId);
            int result = ps.executeUpdate();
            return result == 1;
        } catch (SQLException ex) { }
        return false;
    }
    
    private int getStatementId(int projectId, int statementIndex) {
        if (tableExists(STATEMENTSTABLE)) {
            String sqlQuery = getStatementIdQuery();
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ps.setInt(2, statementIndex);
                ResultSet results = ps.executeQuery();
                if (results.next()) {
                    return results.getInt(1);
                }
            } catch (SQLException ex) { }
        }
        return 0;
    }
    
    private int getCategoryId(int projectId, LeafCategory subcategory) {
        if (tableExists(DBClassifierDao.CATEGORIESTABLE)) {
            String sqlQuery = "SELECT id FROM " + DBClassifierDao.CATEGORIESTABLE
                    + " WHERE project_id = ? AND name = ? AND depth = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ps.setString(2, subcategory.toString());
                ps.setInt(3, 2);
                ResultSet results = ps.executeQuery();
                if (results.next()) {
                    return results.getInt(1);
                }
            } catch (SQLException ex) { }
        }
        return 0;
    }
    
    private int deleteAllProjectStatements(final int projectId) {
        if (tableExists(STATEMENTSTABLE)) {
            String sqlQuery = "DELETE FROM " + STATEMENTSTABLE + " WHERE project_id = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                int result = ps.executeUpdate();
                return result;
            } catch (SQLException ex) { }
        }
        return 0;
    }
    
    private int deleteAllCatsAndStatements(final int projectId) {
        if (tableExists(DBClassifierDao.CATSANDSTATESTABLE)) {
            String sqlQuery = "DELETE FROM " + DBClassifierDao.CATSANDSTATESTABLE + " WHERE project_id = ?";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                int result = ps.executeUpdate();
                return result;
            } catch (SQLException ex) { }
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
            } catch (SQLException ex) { }
        }
        return statement;
    }
    
    private String getSubcategory(final int projectId, final int statementIndex) {
        final int statementId = getStatementId(projectId, statementIndex);
        final int categoryId = getCategoryId(projectId, statementId);
        if (categoryId == 0) {
            return "";
        }
        String sqlQuery = "SELECT name FROM " + DBClassifierDao.CATEGORIESTABLE + " WHERE id = ?";
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
            ps.setInt(1, categoryId);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                String name = results.getString(1);
                return name;
            }
        } catch (SQLException ex) { }
        return "";
    }
    
    private int getCategoryId(final int projectId, final int statementId) {
        if (tableExists(DBClassifierDao.CATSANDSTATESTABLE) == false) {
            return 0;
        }
        String sqlQuery = "SELECT category_id FROM " + DBClassifierDao.CATSANDSTATESTABLE
                + " WHERE project_id = ? AND statement_id = ?";
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
            ps.setInt(1, projectId);
            ps.setInt(2, statementId);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return results.getInt(1);
            }
        } catch (SQLException ex) { }
        return 0;
    }
}
