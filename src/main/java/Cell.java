public class Cell {
    //can use each ate the same moment with |
    public static final int NORMAL = 0;           // 00000
    public static final int BOLD = 1;             // 00001
    public static final int ITALIC = 2;           // 00010
    public static final int UNDERLINE = 4;        // 00100
    public static final int STRIKETHROUGH = 8;    // 01000
    public static final int BLINK = 16;           // 10000


    private char buffer = ' ';
    private int font = NORMAL;
    private int cellFgColour = 0;
    private int cellBgColour = 0;

    public Cell() {
    }

    public Cell(char buffer, int font, int cellFgColour, int cellBgColour) {
        this.buffer = buffer;
        this.font = font;
        this.cellFgColour = cellFgColour;
        this.cellBgColour = cellBgColour;
    }

    public char getBuffer() { return buffer; }
    public void setBuffer(char buffer) { this.buffer = buffer; }

    public int getFont() { return font; }
    public void setFont(int font) { this.font = font; }

    public int getCellFgColour() { return cellFgColour; }
    public void setCellFgColour(int cellFgColour) { this.cellFgColour = cellFgColour; }

    public int getCellBgColour() { return cellBgColour; }
    public void setCellBgColour(int cellBgColour) { this.cellBgColour = cellBgColour; }



    public Cell copy() {
        return new Cell(this.buffer, this.font, this.cellFgColour, this.cellBgColour);
    }

    public void copyFrom(Cell other) {
        this.buffer = other.buffer;
        this.font = other.font;
        this.cellFgColour = other.cellFgColour;
        this.cellBgColour = other.cellBgColour;
    }

    public void setCell(char buffer, int font, int cellFgColour, int cellBgColour) {
        this.buffer = buffer;
        this.font = font;
        this.cellFgColour = cellFgColour;
        this.cellBgColour = cellBgColour;
    }


    public void reset() {
        this.buffer = ' ';
        this.font = 0;
        this.cellFgColour = 0;
        this.cellBgColour = 0;
    }

    public boolean isEmpty() {
        return buffer == ' ' && font == 0 && cellFgColour == 0 && cellBgColour == 0;
    }
}