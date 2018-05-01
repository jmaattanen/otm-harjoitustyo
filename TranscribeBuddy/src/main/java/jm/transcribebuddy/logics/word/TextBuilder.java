package jm.transcribebuddy.logics.word;

/***   This class is responsible for word processing   ***/

import jm.transcribebuddy.logics.storage.Statement;
import java.util.ArrayList;

public class TextBuilder {
    final protected ArrayList<Statement> statements;
    protected int workingIndex;
    
    final static public String BEGINNINGSIGN
            = "\n\n            * * *   The Beginning of the Document   * * *";
    final static public String ENDSIGN
            = "\n\n            * * *   The End of the Document   * * *";
    
    
    public TextBuilder() {
        statements = new ArrayList<>();
        Statement firstStatement = new Statement(null);
        statements.add(firstStatement);
        workingIndex = 0;
    }
    
    
    public ArrayList<Statement> getAllStatements() {
        return statements;
    }
    
    public String getCurrent() {
        return statements.get(workingIndex).toString();
    }
    
    public String getPrev() {
        if (workingIndex == 0) {
            return BEGINNINGSIGN;
        }
        return statements.get(workingIndex - 1).toString();
    }
    
    public String getNext() {
        if (workingIndex == statements.size() - 1) {
            return ENDSIGN;
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
    
    public void setLast(String statement) {
        workingIndex = statements.size() - 1;
        this.set(statement);
    }
    
    public void deleteStatement() {
        statements.remove(workingIndex);
        if (statements.isEmpty()) {
            Statement first = new Statement(null);
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
        Statement newNode = new Statement(null);
        workingIndex++;
        statements.add(workingIndex, newNode);
    }
    
    /* This method divides statement into two parts */
    public boolean splitStatement(String statement, int splitIndex) {
        set(statement);
        if (splitIndex <= 0 || splitIndex >= statement.length()) {
            // invalid parameters
            return false;
        }
        Statement currentNode = statements.get(workingIndex);
        currentNode.set(statement.substring(0, splitIndex));
        // create a new node
        final int endIndex = statement.length();
        String newStatement = statement.substring(splitIndex, endIndex);
        Statement newNode = new Statement(null);
        newNode.set(newStatement);
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
