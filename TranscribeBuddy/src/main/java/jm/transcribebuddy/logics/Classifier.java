package jm.transcribebuddy.logics;

/***   A tree structure for classification of statements   ***/

import java.util.ArrayList;
import jm.transcribebuddy.logics.storage.Category;

public class Classifier {
    final private Category root;
    final private Category highestUndefined;
    
    // Max depth should have value 1 or greater
    final private int maxDepth = 2;
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
    
    public boolean isRealCategory(Category category) {
        if (category == null || category == root) {
            return false;
        }
        return !undefinedName.equals(category.toString());
    }
    
    public boolean isRoot(Category category) {
        // compares only names atm!
        return category == root;
    }
    
    public int getHeightOfTree() {
        return maxDepth;
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
    
    public ArrayList<Category> getHeadcategories() {
        return getCategories(maxDepth - 1);
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
    
    private int getDepth(Category category) {
        int depth = 0;
        while (category != root) {
            category = category.getParent();
            depth++;
        }
        return depth;
    }
    
    private boolean replaceParent(final Category category, Category newParent) {
        if (!isRealCategory(category) || newParent == null) {
            return false;
        }
        if (getDepth(category) != getDepth(newParent) + 1) {
            return false;
        }
        Category oldParent = category.getParent();
        oldParent.removeChild(category);
        // should check if was only child
        category.setParent(newParent);
        newParent.addChild(category);
        return true;
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
        // Create a new subcategory
        Category parent = highestUndefined.getParent();
        Category subcategory = new Category(name, parent);
        parent.addChild(subcategory);
        return subcategory;
    }
    
    public Category addHeadcategory(String name, final Category subcategory) {
//System.out.println("DEBUG addHeadcategory");
        if (name == null || name.isEmpty() || name.equals(undefinedName)
                || maxDepth < 2 || getDepth(subcategory) != maxDepth
        ) {
            return highestUndefined.getParent();
        }
//System.out.println("still going");
        name = Category.getValidName(name);
        ArrayList<Category> headcategories = getCategories(maxDepth - 1);
        for (Category hc : headcategories) {
            if (name.equals(hc.toString())) {
                // Name exists so no addition needed
//System.out.println("found one");
                replaceParent(subcategory, hc);
                return hc;
            }
        }
        // Create a new headcategory
//System.out.println("creating");
        Category parent = highestUndefined.getParent().getParent();
        Category headcategory = new Category(name, parent);
        parent.addChild(headcategory);
        replaceParent(subcategory, headcategory);
        return headcategory;
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
        if (isRealCategory(subcategory) && subcategory.getSize() == 0) {
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
