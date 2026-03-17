import java.util.ArrayList;
import java.util.List;

public class baseTreminal implements TerminalBuffer {
    private final List<TerminalLine> screen;
    private final Cursor cursor;
    private final Font style;
    private final ScrollManager scrollback;

    private final int screenHeight;
    private final int screenWidth;

    public baseTreminal(int height, int width, int maxScroll) {
        this.screenHeight = height;
        this.screenWidth = width;
        this.screen = new ArrayList<>(height);
        this.cursor = new Cursor(width, height);
        this.style = new Font();
        this.scrollback = new ScrollManager(maxScroll);

        for (int i = 0; i < height; i++) {
            screen.add(new TerminalLine(width));
        }
    }

    @Override
    public void setAttribute(int font, int bg, int fg) {
        style.update(font, bg, fg);
    }

    @Override
    public void writeInput(String input) {
        for (char c : input.toCharArray()) {
            placeCell(c);
        }
    }

    public void placeCell(char c) {
        if (c == '\n') {
            nextLine();
            return;
        }

        if (cursor.isAtEndOfLine()) {
            nextLine();
        }

        TerminalLine currentLine = screen.get(cursor.getY());
        Cell cell = currentLine.getCell(cursor.getX());

        cell.setBuffer(c);
        style.applyTo(cell);

        cursor.moveNext();
    }

    public void nextLine() {
        cursor.resetX();
        if (cursor.isAtBottom()) {
            scrollUp();
        } else {
            cursor.moveDown();
        }
    }

    private void scrollUp() {
        TerminalLine oldLine = screen.remove(0);
        scrollback.add(oldLine);
        screen.add(new TerminalLine(screenWidth));
    }

    @Override
    public void insert(String text) {
        for (char c : text.toCharArray()) {
            if (c == '\n') {
                nextLine();
                continue;
            }

            if (cursor.isAtEndOfLine()) {
                nextLine();
            }

            TerminalLine currentLine = screen.get(cursor.getY());

            // 1. Przesuwamy wszystko w prawo od obecnej pozycji kursora
            currentLine.shiftRight(cursor.getX());

            // 2. Wstawiamy znak (standardowo jak w placeCell)
            Cell cell = currentLine.getCell(cursor.getX());
            cell.setBuffer(c);
            style.applyTo(cell);

            cursor.moveNext();
        }
    }


    @Override public int getCursorX() { return cursor.getX(); }
    @Override public int getCursorY() { return cursor.getY(); }
    @Override public void setCursor(int x, int y) { cursor.set(x, y); }

    @Override
    public char getCharAt(int x, int y) {
        if (y < 0 || y >= screenHeight || x < 0 || x >= screenWidth) return ' ';
        return screen.get(y).getCell(x).getBuffer();
    }

    @Override
    public int getAttributesAt(int x, int y) {
        if (y < 0 || y >= screenHeight || x < 0 || x >= screenWidth) return 0;
        return screen.get(y).getCell(x).getFont();
    }
}