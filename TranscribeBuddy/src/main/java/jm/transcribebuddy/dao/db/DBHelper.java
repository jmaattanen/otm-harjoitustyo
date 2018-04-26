package jm.transcribebuddy.dao.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
    
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
    
    public static boolean tableExists(final String tableName, final Connection connection) {
        if (connection == null) {
            return false;
        }
        try {
            DatabaseMetaData dbData = connection.getMetaData();
            ResultSet tables = dbData.getTables(null, null, tableName, null);
            if (tables.next()) {
                return true;
            }
        } catch (SQLException ex) { }
        return false;
    }
}
