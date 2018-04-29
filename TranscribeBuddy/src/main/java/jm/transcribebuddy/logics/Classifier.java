package jm.transcribebuddy.logics;

import java.util.ArrayList;
import jm.transcribebuddy.logics.storage.Category;

public class Classifier {
    final private Category root;
    final private Category highestUndefined;
    
    // Max depth should have value 1 or greater
    final private int maxDepth = 3;
    final String undefinedName = "Undefined";
    
    public Classifier() {
        root = new Category("*ROOT*");
        
        // Create one undefined category for each depth
        Category parent = root;
        for (int i = 0; i < maxDepth; i++) {
            Category child = new Category(undefinedName, parent);
            parent.addChild(child);
            parent = child;
        }
        highestUndefined = parent;
    }
    
    private boolean isRealCategory(Category category) {
        if (category == null || category == root) {
            return false;
        }
        return !undefinedName.equals(category.toString());
    }
    
    public ArrayList<Category> getCategories(final int depth) {
        if (depth < 1 || depth > maxDepth) {
            return new ArrayList<>();
        }
        ArrayList<Category> categories = new ArrayList<>();
        addChildren(categories, root, depth, 1);
        return categories;
    }
    
    public ArrayList<Category> getSubcategories() {
        return getCategories(maxDepth);
    }
    
    private void addChildren(
            ArrayList<Category> result, Category node,
            final int requestedDepth, int depth
    ) {
        ArrayList<Category> children = node.getChildren();
        if (depth >= requestedDepth) {
            result.addAll(children);
            return;
        }
        for (Category c : children) {
            addChildren(result, c, requestedDepth, depth + 1);
        }
    }
    
    public Category addSubcategory(String name) {
        if (name == null || name.isEmpty() || name.equals(undefinedName)) {
            return highestUndefined;
        }
        // Check that name isn't too long
        name = Category.getValidName(name);
        // Subcategory must have a unique name
        ArrayList<Category> subcategories = getCategories(maxDepth);
        for (Category sc : subcategories) {
            if (name.equals(sc.toString())) {
                // Name exists so no addition needed
                return sc;
            }
        }
        // Create a new category
        Category parent = highestUndefined.getParent();
        Category subcategory = new Category(name, parent);
        parent.addChild(subcategory);
        return subcategory;
    }
    
    private void removeCategory(Category category) {
        if (isRealCategory(category)) {
            Category parent = category.getParent();
            parent.removeChild(category);
            if (parent.getChildren().isEmpty()) {
                removeCategory(parent);
            }
        }
    }
    
    public void removeIfEmpty(Category subcategory) {
        if (subcategory != null && subcategory.getSize() == 0) {
            removeCategory(subcategory);
        }
    }
    
    public Category getSubcategory(final String name) {
        if (name == null || name.isEmpty() || name.equals(undefinedName)) {
            return highestUndefined;
        }
        ArrayList<Category> subcategories = getCategories(maxDepth);
        for (Category sc : subcategories) {
            if (name.equals(sc.toString())) {
                return sc;
            }
        }
        // No match
        return highestUndefined;
    }
    
    public Category getUndefinedSubcategory() {
        return highestUndefined;
    }
    
    public Category getUndefined(final int depth) {
        if (depth < 1 || depth > maxDepth) {
            return null;
        }
        Category undefined = highestUndefined;
        for (int i = maxDepth; i > depth; i--) {
            undefined = undefined.getParent();
        }
        return undefined;
    }
    
    public String getUndefinedName() {
        return undefinedName;
    }
    
}
