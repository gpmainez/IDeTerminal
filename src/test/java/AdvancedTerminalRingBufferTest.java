import Terminal.TerminalRingBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedTerminalRingBufferTest {

    private TerminalRingBuffer terminal;

    @BeforeEach
    public void setUp() {
        terminal = new TerminalRingBuffer(5, 10, 10);
    }

    @Test
    public void testMassiveScrollbackAndRingBufferWrap() {
        for (int i = 0; i < 20; i++) {
            terminal.writeInput("Line " + i + "\n");
        }


        assertEquals(4, terminal.getCursorY());
        assertTrue(terminal.line(3).contains("Line 19"), "Screen should contain Line 19 at y=3");
        assertTrue(terminal.line(2).contains("Line 18"), "Screen should contain Line 18 at y=2");

        assertTrue(terminal.line(-10).contains("Line 6"), "Oldest history (y=-10) should be Line 6");


        assertEquals("", terminal.line(-11), "Querying beyond max scrollback should return empty string");
    }

    @Test
    public void testAttributesAndFillLine() {

        terminal.setAttribute(5, 41, 37);
        terminal.fillLine('#');


        assertEquals('#', terminal.getCharAt(0, 0));
        assertEquals('#', terminal.getCharAt(9, 0));
        assertEquals(5, terminal.getAttributesAt(0, 0), "Font attribute should be 5");
        assertEquals(0, terminal.getCursorX(), "Cursor should not move after fillLine");
    }

    @Test
    public void testClearScreenAndClearAll() {
        terminal.writeInput("Hello\nWorld");
        terminal.clearScreen();

        assertEquals(' ', terminal.getCharAt(0, 0), "Screen should be cleared");
        assertEquals(' ', terminal.getCharAt(0, 1), "Screen should be cleared");


        for (int i = 0; i < 10; i++) terminal.writeInput("Data\n");

        terminal.clearAll();

        assertEquals(' ', terminal.getCharAt(0, -1), "Scrollback should be cleared");
        assertEquals(0, terminal.getCursorX());
        assertEquals(0, terminal.getCursorY());
    }

    @Test
    public void testInsertEmptyLine() {
        terminal.writeInput("TopLine");
        terminal.setCursor(0, 4);
        terminal.writeInput("BottomLine");


        terminal.insertEmptyLine();


        assertTrue(terminal.line(3).contains("BottomLine"), "Bottom line should be shifted up");
        assertEquals("          ", terminal.line(4), "New bottom line should be empty");
        assertTrue(terminal.line(-1).contains("TopLine"), "TopLine should be pushed to scrollback");
    }
}