package jm.transcribebuddy.logics.storage;

/**
 * A tree node object for categorizing Statements.
 * The category name maximum length is set to 30. Names are unique.
 * Tree leaves are called 'subcategories' in this project.
 * 
 * @author Juha
 */
public class Category implements Comparable {
    protected String name;
    protected Category parent;
    
    final static private int MAXNAMELENGTH = 30;
    
    /**
     * The root of category tree can be created by this constructor.
     * 
     * @param name Category name. NULL or empty string is
     * changed to "Undefined". Name should have up to 30 characters.
     */
    public Category(String name) {
        this.name = getValidName(name);
        parent = this;
    }
    
    /**
     * 
     * @param name Category name. NULL or empty string is
     * changed to "Undefined". Name should have up to 30 characters.
     * @param parent The upper category to which this belongs.
     */
    public Category(String name, Category parent) {
        this.name = getValidName(name);
        if (parent == null) {
            this.parent = this;
        } else {
            this.parent = parent;
        }
    }
    
    /**
     * This method edits the name to match the desired format. 
     * 
     * @param name Category name. Name should have up to 30 characters.
     * @return "Undefined" if name is NULL or empty string. Otherwise the
     * name in trimmed form.
     */
    public static String getValidName(String name) {
        if (name == null) {
            return "Undefined";
        }
        name = name.trim();
        boolean useFormatting = true;
        while (!name.isEmpty() && name.charAt(0) == '!') {
            useFormatting = false;
            name = name.substring(1, name.length());
        }
        if (name.isEmpty()) {
            return "Undefined";
        }
        if (name.length() > MAXNAMELENGTH) {
            return name.substring(0, MAXNAMELENGTH);
        }
        if (useFormatting) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
        return name;
    }
    
    /**
     * Renames the category. Method won't check if the name is still unique.
     * 
     * @param name Category name. Name should have up to 30 characters.
     * NULL or empty string won't affect.
     * @return True if the category was renamed.
     */
    public boolean rename(String name) {
        if (name == null || name.isEmpty() || name.equals(this.name)) {
            return false;
        }
        this.name = getValidName(name);
        return true;
    }
    
    public Category getParent() {
        return parent;
    }
    
    public void setParent(Category parent) {
        if (parent != null) {
            this.parent = parent;
        }
    }
    
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
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
