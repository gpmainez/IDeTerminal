import Terminal.TerminalRingBuffer;
import Terminal.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductQualityTest {

    private TerminalRingBuffer terminal;
    private final int HEIGHT = 24;

    @BeforeEach
    public void setUp() {
        int WIDTH = 80;
        int SCROLL = 1000;
        terminal = new TerminalRingBuffer(HEIGHT, WIDTH, SCROLL);
    }

    @Test
    public void testEnterpriseGradeWorkflow() {
        terminal.setCursor(79, 0);
        terminal.writeInput("字");

        //wide characters edge cases
        assertEquals('\0', terminal.getCharAt(79, 0), "cell should be padded with space if WideChar doesn't fit");
        assertEquals('字', terminal.getCharAt(0, 1), "wide character should wrap to the beginning of next line");
        assertEquals('\0', terminal.getCharAt(1, 1), "secod cell of WideChar is null marker");



        // check if fonts and styles and colous work together
        terminal.setAttribute(Cell.BOLD | Cell.UNDERLINE, 0, 32);
        terminal.setCursor(0, 5);
        terminal.writeInput("PRO_DATA");

        assertEquals(5, terminal.getAttributesAt(0, 5), "attr should persist in memory correctly");
        assertEquals('P', terminal.getCharAt(0, 5));

        // ringBuffer test
        for (int i = 0; i < 1200; i++) {
            terminal.writeInput("Log entry #" + i + "\n");
        }

        assertEquals(HEIGHT - 1, terminal.getCursorY());

       //data overflow check
        assertFalse(terminal.line(-1000).isEmpty(), "max capacity should still contain data");

        // potential plot holes
        String dump = terminal.currentScreen();
        assertNotNull(dump);
        assertEquals(HEIGHT, dump.split("\n", -1).length, "screen dump number of rows musn`t changed");
    }

    @Test
    public void testDataSafetyOnClear() {
        terminal.writeInput("sensitive Data");
        terminal.clearAll();

        // data erase test
        assertEquals(' ', terminal.getCharAt(0, 0));
        assertEquals("", terminal.line(0).trim());
        assertEquals(0, terminal.getCursorX());
    }
}