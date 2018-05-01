package jm.transcribebuddy.logics.word;

/***   TextBuilder subclass for DAO construction   ***/

import jm.transcribebuddy.logics.storage.Statement;

public class DuctileTextBuilder extends DetailedTextBuilder {
    
    public DuctileTextBuilder() {
        // Start constructing from a blank table
        statements.clear();
        undefined.removeStatement();
    }
    
    public void addNewStatement(Statement newStatement) {
        newStatement.setSubcategory(undefined);
        statements.add(newStatement);
    }
    public boolean isValid() {
        workingIndex = statements.size() - 1;
        return workingIndex >= 0;
    }
}
