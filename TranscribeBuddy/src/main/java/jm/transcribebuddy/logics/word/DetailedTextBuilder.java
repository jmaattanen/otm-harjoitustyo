package jm.transcribebuddy.logics.word;

import java.util.HashMap;
import javafx.util.Duration;
import jm.transcribebuddy.logics.Classifier;
import jm.transcribebuddy.logics.storage.Category;
import jm.transcribebuddy.logics.storage.LeafCategory;
import jm.transcribebuddy.logics.storage.Statement;

/**
 * TextBuilder subclass with time marking and categorizing features.
 * 
 * @see jm.transcribebuddy.logics.word.TextBuilder
 * @see jm.transcribebuddy.logics.storage.Category
 * 
 * @author juham
 */
public class DetailedTextBuilder extends TextBuilder {
    final private Classifier classifier;
    final protected LeafCategory undefined;
    
    
    /**
     * Creates a new DetailedTextBuilder instance and initializes its
     * classifier and first statement. The first statement is classified
     * in "Undefined" sub category.
     * 
     * @see jm.transcribebuddy.logics.Classifier
     */
    public DetailedTextBuilder() {
        classifier = new Classifier();
        undefined = classifier.getUndefinedSubcategory();
        Statement firstStatement = statements.get(0);
        firstStatement.setSubcategory(undefined);
    }
    
    
    public Classifier getClassifier() {
        return classifier;
    }
    
    /**
     * This method returns the name of the sub category the currently
     * active statement belongs to.
     * 
     * @see jm.transcribebuddy.logics.storage.Category
     * @return Sub category name as a string.
     */
    public String getCurrentSubcategory() {
        Statement node = statements.get(workingIndex);
        Category subcategory = node.getSubcategory();
        if (subcategory == null) {
            return classifier.getUndefinedName();
        }
        return subcategory.toString();
    }
    
    /**
     * Returns a hash map of (index, string content) pairs of Statement objects
     * that belong to the given sub category. Index key indicates the
     * location of Statement in the list stored to TextBuilder.
     * 
     * @see jm.transcribebuddy.logics.storage.Category
     * @see jm.transcribebuddy.logics.storage.Statement
     * 
     * @param subcategory The filter of Statement collection.
     * @return HashMap of desired (index, statement) collection.
     */
    public HashMap<Integer, String> getStatementsIn(LeafCategory subcategory) {
        HashMap<Integer, String> resultMap = new HashMap<>();
        if (subcategory == null) {
            return resultMap;
        }
        int index = 0;
        for (Statement s : statements) {
            if (s.isInSubcategory(subcategory)) {
                resultMap.put(index, s.toString());
            }
            index++;
        }
        return resultMap;
    }
    
    /**
     * Puts currently active Statement into given sub category. If
     * categoryName doesn't match to any existing sub category a new
     * sub category will be created. The old sub category will be deleted
     * if no other Statement object belongs to it.
     * 
     * @see jm.transcribebuddy.logics.storage.Category
     * @see jm.transcribebuddy.logics.storage.Statement
     * 
     * @param categoryName Sub category name.
     */
    public void setSubcategory(String categoryName) {
        LeafCategory subcategory = classifier.addSubcategory(categoryName);
        Statement node = statements.get(workingIndex);
        LeafCategory oldCategory = node.getSubcategory();
        node.setSubcategory(subcategory);
        classifier.removeIfEmpty(oldCategory);
    }
    
    public Duration getStartTime() {
        Statement currentStatement = statements.get(workingIndex);
        return currentStatement.getStartTime();
    }
    
    public void setStartTime(Duration startTime) {
        Statement currentStatement = statements.get(workingIndex);
        currentStatement.setStartTime(startTime);
    }
    
    public String startTimeToString() {
        Statement statement = statements.get(workingIndex);
        return statement.startTimeToString();
    }
    
    /**
     * This method removes the currently active Statement object.
     * Method takes care that workingIndex always points to
     * existing index of Statements list of TextBuilder.
     * If the deleted Statement was the only object in the list
     * a new Statement will be created.
     * 
     * @see jm.transcribebuddy.logics.word.TextBuilder#workingIndex
     * @see jm.transcribebuddy.logics.word.TextBuilder#statements
     * @see jm.transcribebuddy.logics.storage.Statement
     */
    @Override
    public void deleteStatement() {
        Statement node = statements.get(workingIndex);
        LeafCategory subcategory = node.getSubcategory();
        subcategory.removeStatement(node);
        classifier.removeIfEmpty(subcategory);
        statements.remove(workingIndex);
        if (statements.isEmpty()) {
            Statement first = new Statement(undefined);
            statements.add(first);
        } else if (workingIndex == statements.size()) {
            // statement in the end of list has been deleted
            // so index must move to prev
            workingIndex--;
        }
    }
    
    /**
     * Copies given string into currently active Statement and creates a
     * new list node to the next index. New Statement object is classified
     * in "Undefined" sub category.
     * 
     * @see jm.transcribebuddy.logics.storage.Category
     * @see jm.transcribebuddy.logics.storage.Statement
     * 
     * @param statement String content to be updated in Statement object
     * before moving on.
     */
    @Override
    public void endStatement(String statement) {
        super.endStatement(statement);
        Statement node = statements.get(workingIndex);
        node.setSubcategory(undefined);
    }
    
    /**
     * This method divides Statement into two.
     * 
     * @see jm.transcribebuddy.logics.storage.Statement
     * 
     * @param statement Updated string value.
     * @param splitIndex Splitting spot.
     * @param splitTime Current time of audio track when the split is made.
     * @return True if the split was made.
     */
    public boolean splitStatement(String statement, int splitIndex, Duration splitTime) {
        boolean result = super.splitStatement(statement, splitIndex);
        if (result == true) {
            setSubcategory(undefined.toString());
            setStartTime(splitTime);
        }
        return result;
    }
    
}
