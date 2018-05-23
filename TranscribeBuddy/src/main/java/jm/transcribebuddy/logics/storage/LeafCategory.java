package jm.transcribebuddy.logics.storage;

/**
 *
 * @author juham
 */
public class LeafCategory extends Category {
    
    public LeafCategory(String name) {
        super(name);
    }
    
    public LeafCategory(String name, Category parent) {
        super(name, parent);
    }
}
