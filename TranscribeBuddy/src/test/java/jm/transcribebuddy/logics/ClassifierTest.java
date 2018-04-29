package jm.transcribebuddy.logics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import jm.transcribebuddy.logics.storage.Category;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

public class ClassifierTest {
    
    Classifier classifier;
    Category highestUndefined;
    
    public ClassifierTest() {
    }

    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        classifier = new Classifier();
        highestUndefined = classifier.getUndefinedSubcategory();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void undefinedSubcategoryIsNotNull() {
        Category undefined = classifier.getUndefinedSubcategory();
        assertNotNull(undefined);
    }
    
    @Test
    public void undefinedExistsAtFirstLevel() {
        Category undefined = classifier.getUndefined(1);
        assertNotNull(undefined);
    }
    
    @Test
    public void undefinedSubcategoryHasName() {
        String undefinedName = classifier.getUndefinedName();
        assertNotNull(undefinedName);
    }
    
    @Test
    public void subcategoriesCanBeAdded1() {
        final String className = "Luokka";
        Category subcategory = classifier.addSubcategory(className);
        assertEquals(className, subcategory.toString());
    }
    
    @Test
    public void subcategoriesCanBeAdded2() {
        classifier.addSubcategory("Luokka1");
        classifier.addSubcategory("Luokka2");
        // There should exist three subcategories now
        // "Luokka1", "Luokka2" and "Undefined"
        ArrayList<Category> subcategories = classifier.getSubcategories();
        assertEquals(3, subcategories.size());
    }
    
    @Test
    public void getCategoriesWontReturnRoot() {
        ArrayList<Category> categories = classifier.getCategories(0);
        assertTrue(categories.isEmpty());
    }
    
    @Test
    public void undefinedCantBeRemoved() {
        classifier.removeIfEmpty(highestUndefined);
        highestUndefined = null;
        highestUndefined = classifier.getUndefinedSubcategory();
        assertNotNull(highestUndefined);
    }
    
    @Test
    public void realCategoriesCanBeRemoved() {
        final String className = "Luokka";
        Category subcategory = classifier.addSubcategory(className);
        classifier.removeIfEmpty(subcategory);
        subcategory = null;
        subcategory = classifier.getSubcategory(className);
        assertEquals(highestUndefined, subcategory);
    }
    
    @Test
    public void emptyNamesAreNotAllowed() {
        classifier.addSubcategory("");
        Category subcategory = classifier.getSubcategory("");
        assertEquals(highestUndefined, subcategory);
    }
    
    @Test
    public void classifierReturnsSubcategoryCalledByName() {
        final String className = "Tekoälylliset";
        classifier.addSubcategory(className);
        Category subcategory = classifier.getSubcategory(className);
        assertEquals(className, subcategory.toString());
    }
    
    @Test
    public void getUndefinedMethodReturnsNullIfTheDepthIsNotValid() {
        int height = classifier.getHeightOfTree();
        Category undefined = classifier.getUndefined(height + 1);
        assertNull(undefined);
    }
    
    @Test
    public void sameCategoryCantBeAddedMultipleTimes() {
        classifier.addSubcategory("peruna");
        classifier.addSubcategory("peruna");
        classifier.addSubcategory("Peruna");
        classifier.addSubcategory("PERUNA");
        classifier.addSubcategory("potato");
        classifier.addSubcategory("  pOtAtO ");
        // Now we should have three subcategories:
        // "Peruna", "Potato" and "Undefined"
        ArrayList<Category> subcategories = classifier.getSubcategories();
        assertEquals(3, subcategories.size());
    }
    
    @Test
    public void removingNullWontThrowException() {
        try {
            classifier.removeIfEmpty(null);
        } catch (NullPointerException e) {
            fail();
        }
    }
    
    @Test
    public void categoriesAreComparable() {
        ArrayList<String> names = new ArrayList<>();
        // "Undefined" belongs to subcategories anyway so must be added here
        names.add("Undefined");
        names.add("Valitus");
        names.add("Anomus");
        names.add("Ylistys");
        names.add("Huvitus");
        final int total = 5;
        for (String name : names) {
            classifier.addSubcategory(name);
        }
        ArrayList<Category> subcategories = classifier.getSubcategories();
        Collections.sort(names);
        Collections.sort(subcategories);
        Iterator<String> name = names.iterator();
        Iterator<Category> cat = subcategories.iterator();
        int matchCounter = 0;
        while (cat.hasNext() && name.hasNext()) {
            if (cat.next().toString().equals(name.next())) {
                matchCounter++;
            }
        }
        assertEquals(total, matchCounter);
    }
}
