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
        for (Statement node : statements) {
            text += node.toString() + " ";
        }
        if (text.isEmpty()) {
            return "";
        }
        // remove the last whitespace
        return text.substring(0, text.length() - 1);
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
    
    /* This method tries to separate the new input */
    public void parseFromAll(final String text) {
        String lastStatement = this.getLast();
        final String firstLettersToSearch = lastStatement.substring(0, lastStatement.length() / 4);
        final int searchForCurrent = text.lastIndexOf(firstLettersToSearch);
        if (lastStatement.equals("") || searchForCurrent < 0 || searchForCurrent >= text.length()) {
            // couldn't find a match for current statement
            // try to find a match for the prev statement
            final String secondLast = this.getSecondLast();
            if (secondLast.equals("")) {
                // assume that the current statement is the first one
                lastStatement = text;
            } else {
                final int searchForPrev = text.lastIndexOf(secondLast);
                if (searchForPrev < 0 || searchForPrev >= text.length()) {
                    // something terrible has happened
                } else {
                    // the second last statement has been found
                    // save all following text to the last statement
                    lastStatement = text.substring(searchForPrev + secondLast.length());
                }
            }
        } else {
            lastStatement = text.substring(searchForCurrent);
        }
        this.setLast(lastStatement);
    }
}
