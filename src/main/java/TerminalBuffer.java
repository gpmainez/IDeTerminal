public interface TerminalBuffer {
    void setAttribute(int form, int bg , int fg);

    void writeInput(String input);

    char getCharAt(int x, int y);

    int getAttributesAt(int x, int y);

    int getCursorX();
    int getCursorY();
    void setCursor(int x, int y);

    void insert(String text);

}
