package jm.transcribebuddy.logics.storage;

/***   An abstract data type for categorizing statements   ***/

import java.util.ArrayList;

public class Category {
    private String name;
    private Category parent;
    private ArrayList<Category> children;
    
    final private int maxNameLength = 30;
    
    public Category(String name) {
        this.name = getValidName(name);
        parent = this;
        children = new ArrayList<>();
    }
    
    public Category(String name, Category parent) {
        this.name = getValidName(name);
        if (parent == null) {
            this.parent = this;
        } else {
            this.parent = parent;
        }
        children = new ArrayList<>();
    }
    
    private String getValidName(String name) {
        if (name == null || name.isEmpty()) {
            return "Undefined";
        }
        if (name.length() > maxNameLength) {
            return name.substring(0, maxNameLength);
        }
        return name;
    }
    
    public boolean rename(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        this.name = getValidName(name);
        return true;
    }
    
    public String getName() {
        return name;
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
}
