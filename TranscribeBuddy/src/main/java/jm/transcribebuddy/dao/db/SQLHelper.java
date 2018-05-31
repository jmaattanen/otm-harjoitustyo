package jm.transcribebuddy.dao.db;

import java.sql.Connection;

/**
 * Interface for getting SQL queries.
 * 
 * @author Juha
 */
public interface SQLHelper {
    
    public Connection connect(String databaseURL, String databaseUser, String databasePass);
    
    public String getCreateProjectsTableQuery();
    public String getInsertProjectQuery();
    
    public String getCreateCategoriesTableQuery();
    public String getInsertCategoryQuery();
    
    public String getCreateStatementsTableQuery();
    public String getInsertStatementQuery();
    public String getLoadStatementQuery();
    public String getStatementIdQuery();
}
