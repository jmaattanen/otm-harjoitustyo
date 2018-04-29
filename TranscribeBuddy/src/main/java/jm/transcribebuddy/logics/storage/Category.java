package jm.transcribebuddy.logics.storage;

/***   A tree node for categorizing statements   ***/

import java.util.ArrayList;

public class Category implements Comparable {
    private String name;
    private Category parent;
    final private ArrayList<Category> children;
    int statementCounter;
    
    final static private int MAXNAMELENGTH = 30;
    
    public Category(String name) {
        this.name = getValidName(name);
        parent = this;
        children = new ArrayList<>();
        statementCounter = 0;
    }
    
    public Category(String name, Category parent) {
        this.name = getValidName(name);
        if (parent == null) {
            this.parent = this;
        } else {
            this.parent = parent;
        }
        children = new ArrayList<>();
        statementCounter = 0;
    }
    
    public static String getValidName(String name) {
        if (name == null || name.isEmpty()) {
            return "Undefined";
        }
        name = name.trim();
        if (name.length() > MAXNAMELENGTH) {
            return name.substring(0, MAXNAMELENGTH);
        }
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return name;
    }
    
    public boolean rename(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        this.name = getValidName(name);
        return true;
    }
    
    public int getSize() {
        return statementCounter;
    }
    
    public Category getParent() {
        return parent;
    }
    
    public ArrayList<Category> getChildren() {
        return children;
    }
    
    public void setParent(Category parent) {
        if (parent != null) {
            this.parent = parent;
        }
    }
    
    public void addChild(Category child) {
        if (child != null) {
            children.add(child);
        }
    }
    
    public boolean removeChild(Category child) {
        if (child.children.isEmpty()) {
            child.parent = null;
            children.remove(child);
            return true;
        }
        return false;
    }
    
    public void addStatement() {
        statementCounter++;
    }
    public void removeStatement() {
        statementCounter--;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!Category.class.isAssignableFrom(object.getClass())) {
            return false;
        }
        final Category other = (Category) object;
        if (this.parent != other.parent) {
            return false;
        }
        if (this.name == null) {
            return null == other.name;
        }
        return this.name.equals(other.name);
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
    
    @Override
    public int compareTo(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        if (!Category.class.isAssignableFrom(object.getClass())) {
            return -1;
        }
        String categoryName = ((Category) object).name;
        return this.name.compareTo(categoryName);
    }
}
