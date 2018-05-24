package jm.transcribebuddy.logics.storage;

import java.util.ArrayList;

/**
 *
 * @author juham
 */
public class InternalCategory extends Category {
    final private ArrayList<Category> children;
    
    
    public InternalCategory(String name) {
        super(name);
        children = new ArrayList<>();
    }
    
    public InternalCategory(String name, Category parent) {
        super(name, parent);
        children = new ArrayList<>();
    }
    
    
    public boolean hasChildren() {
        return !children.isEmpty();
    }
    
    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }
    
    public ArrayList<Category> getChildren() {
        return children;
    }
    
    public void addChild(Category child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
        }
    }
    
    /**
     * Removes the child node from the children of this node.
     * 
     * @see jm.transcribebuddy.logics.storage.Statement
     * @param child The node to be removed.
     * @return True if the child was removed.
     */
    public boolean removeChild(Category child) {
        child.parent = null;
        return children.remove(child);
    }
}
