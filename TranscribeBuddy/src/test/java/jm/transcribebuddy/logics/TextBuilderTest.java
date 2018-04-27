package jm.transcribebuddy.logics;

import jm.transcribebuddy.logics.storage.Statement;
import javafx.util.Duration;
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
        Statement firstStatement = textBuilder.getCurrentStatement();
        assertNotNull(firstStatement);
    }
    
    @Test
    public void firstStatementIsEmpty() {
        String firstStatement = textBuilder.getCurrent();
        assertEquals("", firstStatement);
    }
    
    @Test
    public void firstStatementExistsAfterDelete() {
        textBuilder.deleteStatement();
        // getCurrent will return null if TextBuilder's statements list was empty 
        String str = textBuilder.getCurrent();
        assertEquals("", str);
    }
    
    @Test
    public void deleteMethodAffectsToSelectedStatement() {
        textBuilder.endStatement("Kissa.");
        textBuilder.set("Koira.");
        textBuilder.selectPrev();
        // now the first statement "Kissa." should be selected
        textBuilder.deleteStatement();
        String str = textBuilder.getCurrent();
        assertEquals("Koira.", str);
    }
    
    @Test
    public void startTimeOfFirstStatementIsZero() {
        Duration startTime = textBuilder.getStartTime();
        assertEquals(Duration.ZERO, startTime);
    }
    
    @Test
    public void setMethodTrimsStatement() {
        textBuilder.set("\nTesti lause.  \n\n  ");
        String str = textBuilder.getCurrent();
        assertEquals("Testi lause.", str);
    }
    
    @Test
    public void getPrevNotifiesOfTheBeginning1() {
        for (int i = 0; i < 100; i++) {
            // this does nothing
            textBuilder.selectPrev();
        }
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
    public void splitMethodWorksRight() {
        textBuilder.splitStatement("Muna. Kana. ", 5, Duration.ZERO);
        // now we are working in the second index that should be "Kana."
        String second = textBuilder.getCurrent();
        String first = textBuilder.getPrev();
        String both = second + " " + first;
        assertEquals("Kana. Muna.", both);
    }
    
    @Test
    public void startiTimeToStringWorks1() {
        Duration time = Duration.minutes(1);
        time = time.add(Duration.seconds(1));
        // time should be now one minute and one second
        textBuilder.setStartTime(time);
        String str = textBuilder.startTimeToString();
        assertEquals("01:01", str);
    }
    @Test
    public void startiTimeToStringWorks2() {
        Duration time = Duration.minutes(59);
        time = time.add(Duration.seconds(59));
        textBuilder.setStartTime(time);
        String str = textBuilder.startTimeToString();
        assertEquals("59:59", str);
    }
    @Test
    public void startiTimeToStringWorks3() {
        Duration time = Duration.hours(2);
        time = time.add(Duration.minutes(34));
        time = time.add(Duration.seconds(56));
        textBuilder.setStartTime(time);
        String str = textBuilder.startTimeToString();
        assertEquals("2:34:56", str);
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
    public void emptyStatementsAreDecodedAsLineEndings() {
        textBuilder.endStatement("Yks.");
        textBuilder.endStatement("");
        textBuilder.endStatement("Kaks.");
        textBuilder.endStatement("");
        textBuilder.set("Kol.");
        String text = textBuilder.getAll();
        assertEquals("Yks. \n Kaks. \n Kol.", text);
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
    
    @Test
    public void textBuilderIsNotValidAfterInitialClear() {
        textBuilder.initialClear();
        boolean isValid = textBuilder.isValid();
        assertEquals(false, isValid);
    }
    
    @Test
    public void addNewStatementWorks() {
        //textBuilder has now one statement
        Statement statement = new Statement(null);
        textBuilder.addNewStatement(statement);
        int listSize = textBuilder.getAllStatements().size();
        assertEquals(2, listSize);
    }
    
    
}
