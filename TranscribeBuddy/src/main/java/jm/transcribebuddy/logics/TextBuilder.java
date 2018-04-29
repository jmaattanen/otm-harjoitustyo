package jm.transcribebuddy.logics;

/***   This class is responsible for word processing   ***/

import jm.transcribebuddy.logics.storage.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Duration;
import jm.transcribebuddy.logics.storage.Category;

public class TextBuilder {
    final private ArrayList<Statement> statements;
    private int workingIndex;
    final private Classifier classifier;
    final Category undefined;
    
    public TextBuilder() {
        statements = new ArrayList<>();
        classifier = new Classifier();
        undefined = classifier.getUndefinedSubcategory();
        Statement firstStatement = new Statement(undefined);
        statements.add(firstStatement);
        workingIndex = 0;
    }
    
    /* FOR DAO */
    public Statement getCurrentStatement() {
        return statements.get(workingIndex);
    }
    public ArrayList<Statement> getAllStatements() {
        return statements;
    }
    public void addNewStatement(Statement newStatement) {
        newStatement.setSubcategory(undefined);
        undefined.addStatement();
        statements.add(newStatement);
    }
    public void initialClear() {
        statements.clear();
        undefined.removeStatement();
    }
    public boolean isValid() {
        workingIndex = statements.size() - 1;
        return workingIndex >= 0;
    }
    /* FOR DAO */
    
    public String getCurrent() {
        return statements.get(workingIndex).toString();
    }
    
    public String getCurrentSubcategory() {
        Statement node = statements.get(workingIndex);
        Category subcategory = node.getSubcategory();
        if (subcategory == null) {
            return classifier.getUndefinedName();
        }
        return subcategory.toString();
    }
    
    public Duration getStartTime() {
        Statement currentStatement = statements.get(workingIndex);
        return currentStatement.getStartTime();
    }
    
    public String getPrev() {
        if (workingIndex == 0) {
            return "*Projektin alku*";
        }
        return statements.get(workingIndex - 1).toString();
    }
    
    public String getNext() {
        if (workingIndex == statements.size() - 1) {
            return "*Projektin loppu*";
        }
        return statements.get(workingIndex + 1).toString();
    }
    
    public String getLast() {
        final int index = statements.size() - 1;
        return statements.get(index).toString();
    }
    
    public String getAll() {
        workingIndex = statements.size() - 1;
        String text = "";
        int count = 0;
        for (Statement node : statements) {
            String str = node.toString();
            if (str.isEmpty() && count != workingIndex) {
                // empty statements are decoded as line endings
                // except the last statement
                str = "\n";
            }
            text += str + " ";
            count++;
        }
        text = removeTheLastWhitespace(text);
        return text;
    }
    
    public Classifier getClassifier() {
        return classifier;
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
    
    public String startTimeToString() {
        Statement statement = statements.get(workingIndex);
        return statement.startTimeToString();
    }
    
    public boolean set(String statement) {
        statement = statement.trim();
        Statement node = statements.get(workingIndex);
        String oldStatement = node.toString();
        if (oldStatement.equals(statement)) {
            // return false when nothing has changed
            return false;
        }
        node.set(statement);
        return true;
    }
    
    public void setSubcategory(String categoryName) {
        Category subcategory = classifier.addSubcategory(categoryName);
        Statement node = statements.get(workingIndex);
        Category oldCategory = node.getSubcategory();
        if (oldCategory != subcategory) {
            subcategory.addStatement();
            oldCategory.removeStatement();
            classifier.removeIfEmpty(oldCategory);
        }
        node.setSubcategory(subcategory);
    }
    
    public void setStartTime(Duration startTime) {
        Statement currentStatement = statements.get(workingIndex);
        currentStatement.setStartTime(startTime);
    }
    
    public void setLast(String statement) {
        workingIndex = statements.size() - 1;
        this.set(statement);
    }
    
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
    
    public void endStatement(String statement) {
        Statement node = statements.get(workingIndex);
        node.set(statement);
        Statement newNode = new Statement(undefined);
        workingIndex++;
        statements.add(workingIndex, newNode);
    }
    
    /* This method divides statement into two parts */
    public boolean splitStatement(String statement, int splitIndex, Duration splitTime) {
        if (splitIndex <= 0 || splitIndex >= statement.length()) {
            // invalid parameters
            return false;
        }
        Statement currentNode = statements.get(workingIndex);
        currentNode.set(statement.substring(0, splitIndex));
        // create a new node
        final int endIndex = statement.length();
        String newStatement = statement.substring(splitIndex, endIndex);
        Statement newNode = new Statement(newStatement, splitTime, undefined);
        workingIndex++;
        statements.add(workingIndex, newNode);
        return true;
    }
    
    /* This method analyses text modification over the last statement */
    public int parseFromAll(final String text) {
        String savedText = this.getAll();
        // ignore the last statement
        final int beginIndexOfLastStatement = savedText.length() - this.getLast().length();
        savedText = savedText.substring(0, beginIndexOfLastStatement);
        savedText = removeTheLastWhitespace(savedText);
        
        if (text.length() < savedText.length()) {
            // Something is missing!!
            // Do nothing here
            return -1;
            
        } else if (text.startsWith(savedText) == false) {
            // Something has been edited illegally!!
            // Copy everything into last statement
            this.set(text);
            return -2;
        } else if (text.length() == savedText.length()) {
            // Nothing has changed, everything ok
            this.set("");
            return 0;
        } 
        // Everything ok
        String lastStatement = text.substring(beginIndexOfLastStatement);
        if (this.set(lastStatement) == false) {
            return 0;
        }
        return 1;
    }
    
    public void selectByCaretPosition(final int caretPosition) {
        workingIndex = 0;
        int charCount = 0;
        for (Statement s : statements) {
            String str = s.toString();
            // Each statement is followed by a single whitespace
            charCount += str.length() + 1;
            if (caretPosition >= charCount) {
                workingIndex++;
            } else {
                break;
            }
        }
        if (workingIndex >= statements.size()) {
            workingIndex = statements.size() - 1;
        }
    }
    
    public void selectPrev() {
        if (workingIndex > 0) {
            workingIndex--;
        }
    }
    
    public void selectNext() {
        if (workingIndex < statements.size() - 1) {
            workingIndex++;
        }
    }
    
    private String removeTheLastWhitespace(String text) {
        int lastIndex = text.length() - 1;
        if (lastIndex >= 0 && text.charAt(lastIndex) == ' ') {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }
}
