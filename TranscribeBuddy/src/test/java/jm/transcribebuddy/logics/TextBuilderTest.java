package jm.transcribebuddy.logics;

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
    
    @Test
    public void getPrevNotifiesOfTheBeginning1() {
        String prev = textBuilder.getPrev();
        assertEquals("*Projektin alku*", prev);
    }
    
    @Test
    public void getPrevNotifiesOfTheBeginning2() {
        final String statement = "Testi lause.";
        final int additions = 10;
        for(int i = 0; i < additions; i++)
            textBuilder.endStatement(statement);
        for(int i = 0; i < additions; i++)
            textBuilder.selectPrev();
        // now we should be working with the first statement
        String prev = textBuilder.getPrev();
        assertEquals("*Projektin alku*", prev);
    }
    
    @Test
    public void getNextNotifiesOfTheEnd() {
        final String statement = "Testi lause.";
        final int additions = 10;
        for(int i = 0; i < additions; i++)
            textBuilder.endStatement(statement);
        // now we should be working with the last statement
        String next = textBuilder.getNext();
        assertEquals("*Projektin loppu*", next);
    }
    
    @Test
    public void getAllBuildsTextRight() {
        textBuilder.endStatement("Yks.");
        textBuilder.endStatement("Kaks.");
        textBuilder.set("Kolme.");
        String text = textBuilder.getAll();
        assertEquals("Yks. Kaks. Kolme.", text);
    }
    
    @Test
    public void parseFromAllRecognizesAdditions() {
        textBuilder.endStatement("Eka.");
        textBuilder.endStatement("Toka.");
        String text = textBuilder.getAll();
        text = text + "Uutta tekstiä.";
        // text should be now "Eka. Toka. Uutta tekstiä."
        textBuilder.parseFromAll(text);
        String statement = textBuilder.getCurrent();
        assertEquals("Uutta tekstiä.", statement);
    }
    
    @Test
    public void parseFromAllRecognizesModifications() {
        textBuilder.endStatement("Eka.");
        textBuilder.endStatement("Toka.");
        textBuilder.set("Neljäs.");
        String text = textBuilder.getAll();
        // text should be now "Eka. Toka. Neljäs."
        text = text.substring(0, 11) + "Kolmas.";
        textBuilder.parseFromAll(text);
        String statement = textBuilder.getCurrent();
        assertEquals("Kolmas.", statement);
    }
}
