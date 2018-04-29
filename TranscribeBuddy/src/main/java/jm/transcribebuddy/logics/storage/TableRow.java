package jm.transcribebuddy.logics.storage;

import javafx.beans.property.SimpleStringProperty;

/***   Data structure for the table contents in Overview scene   ***/

public class TableRow {
    private final SimpleStringProperty subcategory = new SimpleStringProperty("");
    private final SimpleStringProperty statement = new SimpleStringProperty("");
    
    // for future implementations
    private final int projectId;
    private final int statementIndex;
    
    public TableRow(String subcategory, String statement, int index) {
        setSubcategory(subcategory);
        setStatement(statement);
        projectId = 0;
        statementIndex = index;
    }
    
    public String getSubcategory() {
        return subcategory.get();
    }
    
    public final void setSubcategory(String subcategory) {
        this.subcategory.set(subcategory);
    }
    
    public String getStatement() {
        return statement.get();
    }
    
    public final void setStatement(String statement) {
        this.statement.set(statement);
    }
    
}
