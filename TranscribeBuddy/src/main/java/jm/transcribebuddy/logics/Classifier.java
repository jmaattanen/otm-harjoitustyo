package jm.transcribebuddy.logics;

import java.util.ArrayList;
import jm.transcribebuddy.logics.storage.Category;

/**
 * A tree structure for statement classification.
 * 
 * @author juham
 */
public class Classifier {
    final private Category root;
    final private Category highestUndefined;
    
    // Max depth should have value 1 or greater
    final private int maxDepth;
    final String undefinedName = "Undefined";
    
    public Classifier() {
        this(2);
    }
    
    // Heigtht limited between 1 and 6
    public Classifier(int heightOfTree) {
        root = new Category("*ROOT*");
        if (heightOfTree < 1) {
            maxDepth = 1;
        } else if (heightOfTree > 6) {
            maxDepth = 6;
        } else {
            maxDepth = heightOfTree;
        }
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
    
    public ArrayList<Category> getSubcategories() {
        return getCategories(maxDepth);
    }
    
    public ArrayList<Category> getHeadcategories() {
        return getCategories(maxDepth - 1);
    }
    
    public ArrayList<Category> getCategories(final int depth) {
        if (depth < 1 || depth > maxDepth) {
            return new ArrayList<>();
        }
        ArrayList<Category> categories = new ArrayList<>();
        addChildren(categories, root, depth, 1);
        return categories;
    }
    
    private void addChildren(
            ArrayList<Category> generation, Category node,
            final int requestedDepth, int depth
    ) {
        ArrayList<Category> children = node.getChildren();
        if (depth >= requestedDepth) {
            generation.addAll(children);
            return;
        }
        for (Category c : children) {
            addChildren(generation, c, requestedDepth, depth + 1);
        }
    }
    
    private int getDepth(final Category category) {
        int depth = 0;
        Category node = category;
        while (node != root) {
            node = node.getParent();
            depth++;
        }
        return depth;
    }
    
    private boolean replaceParent(final Category category, final Category newParent) {
        if (!isRealCategory(category) || newParent == null) {
            return false;
        }
        if (getDepth(category) != getDepth(newParent) + 1) {
            return false;
        }
        Category oldParent = category.getParent();
        oldParent.removeChild(category);
        removeCategoryIfIsLonely(oldParent);
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
        if (name == null || name.isEmpty() || name.equals(undefinedName)
                || maxDepth < 2 || getDepth(subcategory) != maxDepth
        ) {
            return highestUndefined.getParent();
        }
        name = Category.getValidName(name);
        ArrayList<Category> headcategories = getCategories(maxDepth - 1);
        for (Category hc : headcategories) {
            if (name.equals(hc.toString())) {
                // Name exists so no addition needed
                replaceParent(subcategory, hc);
                return hc;
            }
        }
        // Create a new headcategory
        Category parent = highestUndefined.getParent().getParent();
        Category headcategory = new Category(name, parent);
        parent.addChild(headcategory);
        replaceParent(subcategory, headcategory);
        return headcategory;
    }
    
    private void removeCategoryIfIsLonely(Category category) {
        if (isRealCategory(category) && category.hasChildren() == false) {
            Category parent = category.getParent();
            parent.removeChild(category);
            removeCategoryIfIsLonely(parent);
        }
    }
    
    public void removeIfEmpty(Category subcategory) {
        if (isRealCategory(subcategory) && subcategory.getSize() == 0) {
            removeCategoryIfIsLonely(subcategory);
        }
    }
    
    public Category getSubcategory(String name) {
        name = Category.getValidName(name);
        if (name.equals(undefinedName)) {
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
    
    @Override
    public String toString() {
        String categoryTree = "";
        for (int depth = 0; depth <= maxDepth; depth++) {
            categoryTree += "[ ";
            ArrayList<Category> categories = getCategories(depth);
            for (Category c : categories) {
                categoryTree += c.toString() + " | ";
            }
            categoryTree += "]\n";
        }
        return categoryTree;
    }
}
