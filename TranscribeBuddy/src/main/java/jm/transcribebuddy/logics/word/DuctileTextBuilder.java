package jm.transcribebuddy.logics.word;

import jm.transcribebuddy.logics.storage.Statement;

/**
 * TextBuilder subclass. This class is supposed to be used only in
 * DAO package. This class makes it easier to restore a saved state
 * of TextBuilder object.
 * 
 * @see jm.transcribebuddy.logics.word.DetailedTextBuilder
 * 
 * @author Juha
 */
public class DuctileTextBuilder extends DetailedTextBuilder {
    
    /**
     * Constructor creates a new object compatible to DetailedTextBuilder
     * with zero statements.
     * 
     * @see jm.transcribebuddy.logics.word.DetailedTextBuilder
     * @see jm.transcribebuddy.logics.storage.Statement
     */
    public DuctileTextBuilder() {
        // Start constructing from a blank table
        statements.clear();
        undefined.removeStatement();
    }
    
    /**
     * Adds a new Statement object to the end of the list and add it
     * to the "Undefined" sub category.
     * 
     * @see jm.transcribebuddy.logics.storage.Category
     * @see jm.transcribebuddy.logics.storage.Statement
     * @param newStatement Statement to be inserted.
     */
    public void addNewStatement(Statement newStatement) {
        newStatement.setSubcategory(undefined);
        statements.add(newStatement);
    }
    
    /**
     * This method checks if TextBuilder object is usable. It also
     * set the last Statement to be processed.
     * 
     * @return True if TextBuilder has stored at least one Statement.
     */
    public boolean isValid() {
        workingIndex = statements.size() - 1;
        return workingIndex >= 0;
    }
}
