package jm.transcribebuddy.logics;

import java.util.ArrayList;
import jm.transcribebuddy.logics.storage.Category;
import jm.transcribebuddy.logics.storage.InternalCategory;
import jm.transcribebuddy.logics.storage.LeafCategory;

/**
 * A tree structure for statement classification.
 * 
 * @author juham
 */
public class Classifier {
    final private InternalCategory root;
    final private LeafCategory highestUndefined;
    
    // Max depth should have value 1 or greater
    final private int maxDepth;
    final String undefinedName = "Undefined";
    
    public Classifier() {
        this(2);
    }
    
    // Heigtht limited between 1 and 6
    public Classifier(int heightOfTree) {
        root = new InternalCategory("*ROOT*");
        if (heightOfTree < 1) {
            maxDepth = 1;
        } else if (heightOfTree > 6) {
            maxDepth = 6;
        } else {
            maxDepth = heightOfTree;
        }
        // Create one undefined category for each depth
        Category parent = root;
        for (int i = 1; i < maxDepth; i++) {
            Category child = new InternalCategory(undefinedName, parent);
            ((InternalCategory) parent).addChild(child);
            parent = child;
        }
        highestUndefined = new LeafCategory(undefinedName, parent);
        ((InternalCategory) parent).addChild(highestUndefined);
    }
    
    public boolean isRealCategory(Category category) {
        if (category == null || category == root) {
            return false;
        }
        return !undefinedName.equals(category.toString());
    }
    
    public boolean isRoot(Category category) {
        return category == root;
    }
    
    public int getHeightOfTree() {
        return maxDepth;
    }
    
    /**
     * Method returns all tree leaves.
     * 
     * @return List of Categories at the highest level.
     */
    public ArrayList<Category> getSubcategories() {
        return getCategories(maxDepth);
    }
    
    /**
     * Method returns all parents of tree leaves.
     * 
     * @return List of Categories at the second highest level.
     */
    public ArrayList<Category> getHeadcategories() {
        return getCategories(maxDepth - 1);
    }
    
    
    /**
     * Method returns all nodes at the requested level.
     * @param depth The distance to the root.
     * @return List of Categories.
     */
    public ArrayList<Category> getCategories(final int depth) {
        if (depth < 1 || depth > maxDepth) {
            return new ArrayList<>();
        }
        ArrayList<Category> categories = new ArrayList<>();
        addChildren(categories, root, depth, 1);
        return categories;
    }
    
    private void addChildren(
            ArrayList<Category> generation, InternalCategory node,
            final int requestedDepth, int depth
    ) {
        ArrayList<Category> children = node.getChildren();
        if (depth >= requestedDepth) {
            generation.addAll(children);
            return;
        }
        for (Category c : children) {
            if (c instanceof InternalCategory) {
                addChildren(generation, (InternalCategory)c, requestedDepth, depth + 1);
            }
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
    
    private boolean replaceParent(final Category category, final InternalCategory newParent) {
        if (!isRealCategory(category) || newParent == null) {
            return false;
        }
        if (getDepth(category) != getDepth(newParent) + 1) {
            return false;
        }
        InternalCategory oldParent = (InternalCategory) category.getParent();
        if (oldParent == newParent) {
            return false;
        }
        oldParent.removeChild(category);
        removeCategoryIfIsLonely(oldParent);
        category.setParent(newParent);
        newParent.addChild(category);
        return true;
    }
    
    /**
     * Adds a new sub category to the tree or returns existing Category
     * with given name. A new sub category becomes a child of Undefined
     * parent.
     * @param name Category name
     * @return The added sub category or undefined sub category if
     * an error occurred
     */
    public Category addSubcategory(final String name) {
        if (name == null || name.isEmpty() || name.equals(undefinedName)) {
            return highestUndefined;
        }
        // Force automatic name formatting by applying method twice
        String formattedName = Category.getValidName(Category.getValidName(name));
        // Subcategory must have a unique name
        ArrayList<Category> subcategories = getCategories(maxDepth);
        for (Category sc : subcategories) {
            String otherFormattedName = Category.getValidName(sc.toString());
            if (formattedName.equals(otherFormattedName)) {
                // Name exists so no addition needed
                return sc;
            }
        }
        // Create a new subcategory
        InternalCategory parent = (InternalCategory) highestUndefined.getParent();
        Category subcategory = new LeafCategory(name, parent);
        parent.addChild(subcategory);
        return subcategory;
    }
    
    /**
     * Adds a new head category to the tree or returns existing Category
     * with given name. A new head category becomes a child of Undefined
     * parent.
     * @param name Category name
     * @param subcategory Child to this head category
     * @return The added head category or undefined sub category if
     * an error occurred
     */
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
                replaceParent(subcategory, (InternalCategory) hc);
                return hc;
            }
        }
        // Create a new headcategory
        InternalCategory parent = (InternalCategory) highestUndefined.getParent().getParent();
        InternalCategory headcategory = new InternalCategory(name, parent);
        parent.addChild(headcategory);
        replaceParent(subcategory, headcategory);
        return headcategory;
    }
    
    private void removeCategoryIfIsLonely(Category category) {
        if (category instanceof InternalCategory && ((InternalCategory) category).hasChildren()) {
            return;
        }
        if (isRealCategory(category)) {
            InternalCategory parent = (InternalCategory) category.getParent();
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
