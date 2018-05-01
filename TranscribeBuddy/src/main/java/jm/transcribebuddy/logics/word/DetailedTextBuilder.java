package jm.transcribebuddy.logics.word;

/***   TextBuilder subclass with time marking and categorizing features   ***/

import java.util.HashMap;
import javafx.util.Duration;
import jm.transcribebuddy.logics.Classifier;
import jm.transcribebuddy.logics.storage.Category;
import jm.transcribebuddy.logics.storage.Statement;

public class DetailedTextBuilder extends TextBuilder {
    final private Classifier classifier;
    final protected Category undefined;
    
    
    public DetailedTextBuilder() {
        classifier = new Classifier();
        undefined = classifier.getUndefinedSubcategory();
        Statement firstStatement = statements.get(0);
        firstStatement.setSubcategory(undefined);
    }
    
    
    public Classifier getClassifier() {
        return classifier;
    }
    
    public String getCurrentSubcategory() {
        Statement node = statements.get(workingIndex);
        Category subcategory = node.getSubcategory();
        if (subcategory == null) {
            return classifier.getUndefinedName();
        }
        return subcategory.toString();
    }
    
    public HashMap<Integer, String> getStatementsIn(Category subcategory) {
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
    
    public void setSubcategory(String categoryName) {
        Category subcategory = classifier.addSubcategory(categoryName);
        Statement node = statements.get(workingIndex);
        Category oldCategory = node.getSubcategory();
        node.setSubcategory(subcategory);
        classifier.removeIfEmpty(oldCategory);
    }
    
    public void setStartTime(Duration startTime) {
        Statement currentStatement = statements.get(workingIndex);
        currentStatement.setStartTime(startTime);
    }
    
    public String startTimeToString() {
        Statement statement = statements.get(workingIndex);
        return statement.startTimeToString();
    }
    
    
    @Override
    public void deleteStatement() {
        Statement node = statements.get(workingIndex);
        Category subcategory = node.getSubcategory();
        subcategory.removeStatement();
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
    
    @Override
    public void endStatement(String statement) {
        Statement node = statements.get(workingIndex);
        node.set(statement);
        Statement newNode = new Statement(undefined);
        workingIndex++;
        statements.add(workingIndex, newNode);
    }
    
    public boolean splitStatement(String statement, int splitIndex, Duration splitTime) {
        boolean result = super.splitStatement(statement, splitIndex);
        if (result == true) {
            setSubcategory(undefined.toString());
            setStartTime(splitTime);
        }
        return result;
    }
    
}
