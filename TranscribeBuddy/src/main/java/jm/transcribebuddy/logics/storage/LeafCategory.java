package jm.transcribebuddy.logics.storage;

/**
 *
 * @author juham
 */
public class LeafCategory extends Category {
    private int statementCounter;
    
    public LeafCategory(String name) {
        super(name);
        statementCounter = 0;
    }
    
    public LeafCategory(String name, Category parent) {
        super(name, parent);
        statementCounter = 0;
    }
    
    
    /**
     * 
     * @return The number of statements in this sub category.
     */
    public int getSize() {
        return statementCounter;
    }
    
    @Override
    public boolean isEmpty() {
        return statementCounter == 0;
    }
    
    /**
     * Call this method when a Statement is added to this sub category.
     * @see jm.transcribebuddy.logics.storage.Statement
     */
    public void addStatement() {
        statementCounter++;
    }
    /**
     * Call this method when a Statement is removed from this sub category.
     * @see jm.transcribebuddy.logics.storage.Statement
     */
    public void removeStatement() {
        statementCounter--;
    }
    
}
