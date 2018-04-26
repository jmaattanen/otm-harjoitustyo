package jm.transcribebuddy.dao.db;

/***   This is DAO that is responsible for storing text info like time marks    ***/

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jm.transcribebuddy.dao.TextInfoDao;
import jm.transcribebuddy.logics.ProjectInfo;
import jm.transcribebuddy.logics.Statement;
import jm.transcribebuddy.logics.TextBuilder;

public class DBTextInfoDao implements TextInfoDao {
    
    private Connection dbConnection;
    final private String databaseURL;
    final private String databaseUser;
    final private String databasePass;
    
    private final String dbTableNameForStatements = "tb_statements";
    
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
    public TextBuilder load(final ProjectInfo projectInfo, final TextBuilder textBuilder) {
        if (connectDatabase() == false) {
            return textBuilder;
        }
        final int projectId = projectInfo.getId();
        
        // Construct a new TextBuilder with loaded text info
        TextBuilder updatedTextBuilder = new TextBuilder();
        updatedTextBuilder.initialClear();

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
        try {
            Class.forName("org.postgresql.Driver");
            dbConnection = DriverManager.getConnection(databaseURL, databaseUser, databasePass);
            return dbConnection != null;
        } catch (SQLException | ClassNotFoundException ex) { }
        return false;
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
                    + "text varchar(8192) NOT NULL, \n"
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
                ps.setString(3, statement.toString());
                ps.setDouble(4, statement.startTimeToDouble());
                int result = ps.executeUpdate();
                return result == 1;
            } catch (SQLException ex) {
//                System.out.println("Failed to insert into " + dbTableNameForStatements + "\n" + ex);
            }
        }
        return false;
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
    
    private boolean tableExists(String tableName) {
        if (dbConnection == null) {
            return false;
        }
        try {
            DatabaseMetaData dbData = dbConnection.getMetaData();
            ResultSet tables = dbData.getTables(null, null, tableName, null);
            if (tables.next()) {
                return true;
            }
        } catch (SQLException ex) {
//            System.out.println("Failed to check if " + tableName + " exists\n" + ex);
        }
        return false;
    }
}
