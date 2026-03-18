import Terminal.TerminalRingBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicTerminalRingBufferTest {

    private TerminalRingBuffer terminal;

    @BeforeEach
    public void setUp() {

        terminal = new TerminalRingBuffer(5, 10, 10);
    }

    @Test
    public void testBasicWriteAndCursorMovement() {

        terminal.writeInput("input");

        assertEquals(5, terminal.getCursorX(), "cursorX should be 5 after writing 5 chars");
        assertEquals(0, terminal.getCursorY(), "cursorY line should be 0");


        assertEquals('i', terminal.getCharAt(0, 0));
        assertEquals('n', terminal.getCharAt(1, 0));
        assertEquals('t', terminal.getCharAt(4, 0));

        assertEquals(' ', terminal.getCharAt(5, 0));
    }

    @Test
    public void testLineWrappingAndNewLine() {
        terminal.writeInput("13charInputYX");

        assertEquals('t', terminal.getCharAt(0, 1), "tYX went to the next line");
        assertEquals(3, terminal.getCursorX(), "Cursor should be at column 3 on the new line");
        assertEquals(1, terminal.getCursorY(), "Cursor Y should be 1");

        terminal.writeInput("\nA");
        assertEquals(2, terminal.getCursorY(), "next line (cursor y) after \\n");
        assertEquals('A', terminal.getCharAt(0, 2), "'A' should be [0] in 3 line");
    }

    @Test
    public void testInsertWithCascadeRippleEffect() {

        terminal.writeInput("FirstInput");
        terminal.setCursor(0, 0);
        terminal.insert("G");

        assertEquals('G', terminal.getCharAt(0, 0));
        assertEquals('F', terminal.getCharAt(1, 0));
        assertEquals('t', terminal.getCharAt(0, 1), "The 't' should [0] in next line");

        // cursor in the first line check
        assertEquals(1, terminal.getCursorX());
        assertEquals(0, terminal.getCursorY());
    }

    @Test
    public void testRingBufferScrollbackHistory() {
        terminal.writeInput("line 1\n");
        terminal.writeInput("line 2\n");
        terminal.writeInput("line 3\n");
        terminal.writeInput("line 4\n");
        terminal.writeInput("line 5\n");
        terminal.writeInput("line 6");


        assertEquals(4, terminal.getCursorY(), "should be in the bottom of the screen");

        // create history(scrollback)
        assertEquals('l', terminal.getCharAt(0, -1), "scrollback at Y=-1 should contain 'L' from Line 1");
        assertEquals('1', terminal.getCharAt(5, -1), "scrollback at Y=-1 should contain '1' from Line 1");

        assertEquals('2', terminal.getCharAt(5, 0), "First line that we see is the 2nd line in our buffer");

        // check screen index
        String screenString = terminal.currentScreen();
        assertTrue(screenString.contains("line 2"), "Screen should contain line 2");
        assertTrue(screenString.contains("line 6"), "Screen should contain line 6");
        assertFalse(screenString.contains("line 1"), "Screen iwthout line 1");
    }

    @Test
    public void testEmptyScrollbackBoundary() {
        //if empty
        char c = terminal.getCharAt(0, -5);
        assertEquals(' ', c, "querying empty scrollback should return space");

        // attrbiutes if empty
        int attr = terminal.getAttributesAt(0, -5);
        assertEquals(0, attr, "querying empty scrollback attributes should return 0");
    }
}