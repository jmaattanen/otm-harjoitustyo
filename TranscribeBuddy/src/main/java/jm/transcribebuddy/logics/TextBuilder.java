package jm.transcribebuddy.logics;

import java.util.ArrayList;
import javafx.util.Duration;

public class TextBuilder {
    final private ArrayList<Statement> statements;
    private int workingIndex;
    
    public TextBuilder() {
        statements = new ArrayList<>();
        Statement firstStatement = new Statement();
        statements.add(firstStatement);
        workingIndex = 0;
    }
    
    public String getCurrent() {
        return statements.get(workingIndex).toString();
    }
    
    /* FOR DAO */
    public Statement getCurrentStatement() {
        return statements.get(workingIndex);
    }
    public ArrayList<Statement> getAllStatements() {
        return statements;
    }
    public void addNewStatement(Statement newStatement) {
        statements.add(newStatement);
    }
    public void initialClear() {
        statements.clear();
    }
    public boolean isValid() {
        workingIndex = statements.size() - 1;
        return workingIndex >= 0;
    }
    /* FOR DAO */
    
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
    
    public String getSecondLast() {
        final int index = statements.size() - 2;
        if (index < 0) {
            return "";
        }
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
    
    public String startTimeToString() {
        Statement statement = statements.get(workingIndex);
        return statement.startTimeToString();
    }
    
    public void set(String statement) {
        Statement node = statements.get(workingIndex);
        node.set(statement.trim());
    }
    
    public void setStartTime(Duration startTime) {
        Statement currentStatement = statements.get(workingIndex);
        currentStatement.setStartTime(startTime);
    }
    
    public void setLast(String statement) {
        workingIndex = statements.size() - 1;
        this.set(statement.trim());
    }
    
    public void deleteStatement() {
        statements.remove(workingIndex);
        if (statements.isEmpty()) {
            Statement first = new Statement();
            statements.add(first);
        } else if (workingIndex == statements.size()) {
            // statement in the end of list has been deleted
            // so index must move to prev
            workingIndex--;
        }
    }
    
    /* This method divides statement into two parts */
    public boolean splitStatement(String statement, int splitIndex, Duration splitTime) {
        if (splitIndex <= 0 || splitIndex >= statement.length()) {
            // invalid parameters
            return false;
        }
        Statement currentNode = statements.get(workingIndex);
        currentNode.set(statement.substring(0, splitIndex).trim());
        // create a new node
        final int endIndex = statement.length();
        String newStatement = statement.substring(splitIndex, endIndex).trim();
        Statement newNode = new Statement(newStatement, splitTime);
        workingIndex++;
        statements.add(workingIndex, newNode);
        return true;
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
    
    public void endStatement(String statement) {
        Statement node = statements.get(workingIndex);
        node.set(statement.trim());
        Statement newNode = new Statement();
        workingIndex++;
        statements.add(workingIndex, newNode);
    }
    
    /* This method analyses text modification over the last statement */
    public boolean parseFromAll(final String text) {
        String savedText = this.getAll();
        // ignore the last statement
        final int beginIndexOfLastStatement = savedText.length() - this.getLast().length();
        savedText = savedText.substring(0, beginIndexOfLastStatement);
        savedText = removeTheLastWhitespace(savedText);
        
        if (text.length() < savedText.length()) {
            // Something is missing!!
            // Do nothing here
            return false;
            
        } else if (text.startsWith(savedText) == false) {
            // Something has been edited illegally!!
            // Copy everything into last statement
            this.set(text);
            return false;
        } else if (text.length() == savedText.length()) {
            // Everything ok
            this.set("");
        } else {
            // Everything ok
            String lastStatement = text.substring(beginIndexOfLastStatement);
            this.set(lastStatement);
        }
        return true;
    }
    
    private String removeTheLastWhitespace(String text) {
        int lastIndex = text.length() - 1;
        if (lastIndex >= 0 && text.charAt(lastIndex) == ' ') {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }
}
