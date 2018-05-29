package jm.transcribebuddy.logics.storage;

import java.util.ArrayList;

/**
 *
 * @author juham
 */
public class LeafCategory extends Category {
    ArrayList<Statement> statements;
    
    public LeafCategory(String name) {
        super(name);
        statements = new ArrayList<>();
    }
    
    public LeafCategory(String name, Category parent) {
        super(name, parent);
        statements = new ArrayList<>();
    }
    
    
    /**
     * 
     * @return The number of statements in this sub category.
     */
    public int getSize() {
        return statements.size();
    }
    
    @Override
    public boolean isEmpty() {
        return statements.isEmpty();
    }
    
    /**
     * Call this method when a Statement is added to this sub category.
     * @see jm.transcribebuddy.logics.storage.Statement
     * @param statement 
     */
    public void addStatement(Statement statement) {
        statements.add(statement);
    }
    /**
     * Call this method when a Statement is removed from this sub category.
     * @see jm.transcribebuddy.logics.storage.Statement
     * @param statement 
     */
    public void removeStatement(Statement statement) {
        statements.remove(statement);
    }
    
}
