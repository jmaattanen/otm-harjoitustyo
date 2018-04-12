package jm.transcribebuddy.logics;

import java.util.ArrayList;

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
    
    public void setLast(String statement) {
        workingIndex = statements.size() - 1;
        this.set(statement.trim());
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
        workingIndex++;
        if (workingIndex == statements.size()) {
            // add a new node to the end
            statements.add(new Statement());
        } else {
            // insert a new node
            statements.add(workingIndex, new Statement());
        }
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
