package jm.transcribebuddy.logics;

import jm.transcribebuddy.logics.TextBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TextBuilderTest {
    
    TextBuilder textBuilder;
    
    public TextBuilderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        textBuilder = new TextBuilder();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void constructorCreatesTheFirstStatement() {
        String firstStatement = textBuilder.getCurrent();
        assertEquals("", firstStatement);
    }
    
    /**
     * Test of getCurrent method, of class TextBuilder.
     */
//    @Test
//    public void testGetCurrent() {
//        System.out.println("getCurrent");
//        TextBuilder instance = new TextBuilder();
//        String expResult = "";
//        String result = instance.getCurrent();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getPrev method, of class TextBuilder.
     */
//    @Test
//    public void testGetPrev() {
//        System.out.println("getPrev");
//        TextBuilder instance = new TextBuilder();
//        String expResult = "";
//        String result = instance.getPrev();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getNext method, of class TextBuilder.
     */
//    @Test
//    public void testGetNext() {
//        System.out.println("getNext");
//        TextBuilder instance = new TextBuilder();
//        String expResult = "";
//        String result = instance.getNext();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getAll method, of class TextBuilder.
     */
//    @Test
//    public void testGetAll() {
//        System.out.println("getAll");
//        TextBuilder instance = new TextBuilder();
//        String expResult = "";
//        String result = instance.getAll();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of set method, of class TextBuilder.
     */
//    @Test
//    public void testSet() {
//        System.out.println("set");
//        String statement = "";
//        TextBuilder instance = new TextBuilder();
//        instance.set(statement);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
