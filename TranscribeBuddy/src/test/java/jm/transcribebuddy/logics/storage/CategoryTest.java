package jm.transcribebuddy.logics.storage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CategoryTest {
    
    Category category;
    
    public CategoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        category = new Category("");
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void nameIsNotIdentifierForCategory() {
        Category cat1 = new Category("Cat");
        Category cat2 = new Category("Cat");
        assertNotEquals(cat1, cat2);
    }
    
    @Test
    public void equalsMethodWorksRight() {
        assertNotEquals(null, category);
        Category cat1 = new Category("Cat", category);
        Category cat2 = new Category("Class", category);
        assertNotEquals(cat1, cat2);
        cat2.rename("Cat");
        // Same names and parent so these are equal
        assertEquals(cat1, cat2);
    }
    
    @Test
    public void parentNameIsNotIdentifierForChild() {
        Category head1 = new Category("Head", category);
        Category head2 = new Category("Head", category);
        // Heads are equal
        assertEquals(head1, head2);
        Category sub1 = new Category("Sub", head1);
        Category sub2 = new Category("Sub", head2);
        // Subs still have different parents
        assertNotEquals(sub1, sub2);
    }
    
    @Test
    public void categoryDoesNotEqualToString() {
        String catName = category.toString();
        assertFalse(category.equals(catName));
    }
    
    @Test
    public void compareToNullThrowsException() {
        try {
            category.compareTo(null);
            fail();
        } catch (NullPointerException e) {
//            System.out.println("YEAH!");
        }
    }
    
    @Test
    public void categoryCanBeRenamed() {
        category.rename("Uusi alku");
        assertEquals("Uusi alku", category.toString());
        category.rename("  oikeinkirjoituksen korjaaminen");
        try {
            category.rename(null);
        } catch (NullPointerException e) {
            fail();
        }
        assertEquals("Oikeinkirjoituksen korjaaminen", category.toString());
        String longName = "";
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                longName += Integer.toString(j);
            }
        }
        // category name length is limited to 30 characters
        category.rename(longName);
        assertEquals("012345678901234567890123456789", category.toString());
    }
    
    @Test
    public void childCanBeAdded() {
        assertFalse(category.hasChildren());
        Category child = new Category("Lapsonen", category);
        category.addChild(child);
        assertTrue(category.hasChildren());
    }
    
    @Test
    public void nameFormattingWorks() {
        // Here automatic name formatting will be executed
        String name = Category.getValidName("ATK-taidot");
        assertEquals("Atk-taidot", name);
        // !-command forces to skip the formatting
        name = Category.getValidName("!ATK-taidot");
        assertEquals("ATK-taidot", name);
        name = Category.getValidName(null);
        assertEquals("Undefined", name);
        name = Category.getValidName("!");
        assertEquals("Undefined", name);
        name = Category.getValidName("!!");
        assertEquals("!", name);
        name = Category.getValidName("  !  Sisennetty");
        assertEquals("  Sisennetty", name);
    }
    
}
