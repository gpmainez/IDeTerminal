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
        // Test stresowy: wpisujemy 20 linii do bufora o pojemności 15.
        // Najstarsze linie zostaną bezpowrotnie nadpisane!
        for (int i = 0; i < 20; i++) {
            terminal.writeInput("Line " + i + "\n");
        }

        // Kursor powinien być na dole nowej, pustej linii (po ostatnim \n w "Line 19")
        assertEquals(4, terminal.getCursorY());

        // Ekran (y od 0 do 4)
        // y=4 to nowa pusta linia, y=3 to "Line 19", y=2 to "Line 18"
        assertTrue(terminal.line(3).contains("Line 19"), "Screen should contain Line 19 at y=3");
        assertTrue(terminal.line(2).contains("Line 18"), "Screen should contain Line 18 at y=2");

        // Sprawdzamy historię (scrollback). Może pomieścić 10 linii.
        // Skoro na y=0 jest "Line 16", to na y=-10 (najstarsza możliwa historia) jest "Line 6"
        assertTrue(terminal.line(-10).contains("Line 6"), "Oldest history (y=-10) should be Line 6");

        // Próba odczytu y = -11 powinna zwrócić puste miejsce (poza zasięgiem)
        assertEquals("", terminal.line(-11), "Querying beyond max scrollback should return empty string");
    }

    @Test
    public void testAttributesAndFillLine() {
        // Ustawiamy atrybuty (np. wyobraźmy sobie, że 5 to maska dla BOLD i UNDERLINE)
        terminal.setAttribute(5, 41, 37);

        // Kursor jest na (0,0). Wypełniamy linię znakiem '#'
        terminal.fillLine('#');

        // Sprawdzamy czy linia została wypełniona
        assertEquals('#', terminal.getCharAt(0, 0));
        assertEquals('#', terminal.getCharAt(9, 0)); // Ostatni znak w linii

        // Sprawdzamy, czy atrybuty zostały nałożone na wygenerowane znaki
        assertEquals(5, terminal.getAttributesAt(0, 0), "Font attribute should be 5");

        // Kursor NIE powinien zmienić pozycji po fillLine
        assertEquals(0, terminal.getCursorX(), "Cursor should not move after fillLine");
    }

    @Test
    public void testClearScreenAndClearAll() {
        terminal.writeInput("Hello\nWorld");

        // Czyścimy tylko widoczny ekran
        terminal.clearScreen();
        assertEquals(' ', terminal.getCharAt(0, 0), "Screen should be cleared");
        assertEquals(' ', terminal.getCharAt(0, 1), "Screen should be cleared");

        // Piszemy znowu, żeby wygenerować historię
        for (int i = 0; i < 10; i++) terminal.writeInput("Data\n");

        // Czyścimy WSZYSTKO (Twardy reset)
        terminal.clearAll();

        // Historia powinna być pusta
        assertEquals(' ', terminal.getCharAt(0, -1), "Scrollback should be cleared");
        // Kursor powinien wrócić na (0,0)
        assertEquals(0, terminal.getCursorX());
        assertEquals(0, terminal.getCursorY());
    }

    @Test
    public void testInsertEmptyLine() {
        terminal.writeInput("TopLine");
        terminal.setCursor(0, 4); // Idziemy na sam dół ekranu
        terminal.writeInput("BottomLine");

        // Wstawiamy pustą linię (to przesuwa tekst do góry)
        terminal.insertEmptyLine();

        // "BottomLine" powinno pojechać do góry (y=3)
        assertTrue(terminal.line(3).contains("BottomLine"), "Bottom line should be shifted up");
        // Nowa dolna linia (y=4) powinna być pusta
        assertEquals("          ", terminal.line(4), "New bottom line should be empty");
        // "TopLine" powinno trafić do historii (y=-1)
        assertTrue(terminal.line(-1).contains("TopLine"), "TopLine should be pushed to scrollback");
    }
}