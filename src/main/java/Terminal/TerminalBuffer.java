package Terminal;

public interface TerminalBuffer {
    void setAttribute(int form, int bg , int fg);

    //text methods
    void writeInput(String input);
    char getCharAt(int x, int y);
    int getAttributesAt(int x, int y);
    void insert(String text);

    //cursor mngment methods
    int getCursorX();
    int getCursorY();
    void setCursor(int x, int y);

    //user mngment
    void fillLine(char c);
    void insertEmptyLine();
    void clearScreen();
    void clearAll();

    //sctring return methpds
    String line(int y);
    String currentScreen();
    String historyBuffer();

}
