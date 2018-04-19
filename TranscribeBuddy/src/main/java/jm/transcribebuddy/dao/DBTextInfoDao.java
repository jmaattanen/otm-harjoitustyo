package jm.transcribebuddy.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jm.transcribebuddy.logics.Statement;
import jm.transcribebuddy.logics.TextBuilder;

public class DBTextInfoDao implements TextInfoDao {
    
    private Connection dbConnection;
    private String databaseUrl;
    private String databaseUser;
    private String databasePass;
    
    private final String STATEMENT_TABLE = "tb_statement";
    
    public DBTextInfoDao() {
        databaseUrl = "jdbc:postgresql://localhost:5432/mytestdb";
        databaseUser = "postgres";
        databasePass = "mayipass";
        
        // create database table if not exists
        if (connectDatabase()) {
            createStatementTable();
            closeConnection();
        }
    }
    
    @Override
    public boolean save(final int projectId, TextBuilder textBuilder) {
        if (connectDatabase() == false) {
            return false;
        }
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
    public TextBuilder loadTextInfo(final int projectId, TextBuilder textBuilder) {
        return null;
    }
    
    public boolean testConnection() {
        boolean result = connectDatabase();
        closeConnection();
        return result;
    }
    
    private boolean connectDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            dbConnection = DriverManager.getConnection(databaseUrl, databaseUser, databasePass);
            return dbConnection != null;
        } catch (SQLException ex) {
            System.out.println("Failed to connect database\n" + ex);
        }
        catch (ClassNotFoundException e) {
            e.getMessage();
        }
        return false;
    }
    
    private void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException ex) {
                System.out.println("Failed to close database connection\n" + ex);
            }
        }
    }
    
    private boolean createStatementTable() {
        if (dbConnection == null) {
            return false;
        }
        try {
            String qs = "CREATE TABLE IF NOT EXISTS " + STATEMENT_TABLE + " (\n"
                    + "project_id integer NOT NULL, \n"
                    + "id integer NOT NULL, \n"
                    + "text varchar(8192) NOT NULL, \n"
                    + "start_time double precision, \n"
                    + "PRIMARY KEY(id) \n"
                    + ");";
            PreparedStatement ps = dbConnection.prepareStatement(qs);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            System.out.println("Failed to create " + STATEMENT_TABLE + " table\n" + ex);
        }
        return false;
    }
    
    private boolean insertStatement(final int projectId, final int statementId, Statement statement) {
        if (tableExists(STATEMENT_TABLE)) {
            String sqlQuery = "INSERT INTO " + STATEMENT_TABLE
                    + " (project_id, id, text, start_time) VALUES (?, ?, ?, ?) "
                    + "ON CONFLICT DO NOTHING";
            try {
                PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
                ps.setInt(1, projectId);
                ps.setInt(2, statementId);
                ps.setString(3, statement.toString());
                ps.setDouble(4, statement.startTimeToDouble());
                int result = ps.executeUpdate();
                return true;
            } catch (SQLException ex) {
                System.out.println("Failed to insert into " + STATEMENT_TABLE + "\n" + ex);
            }
        }
        return false;
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
            System.out.println("Failed to check if " + tableName + " exists\n" + ex);
        }
        return false;
    }
}
