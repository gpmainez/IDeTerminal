public class Cell {
    private char buffer = ' ';
    private int font = 0;
    private int cellFgColour = 0;
    private int cellBgColour = 0;

    public Cell() {
    }

    public Cell(int font, int colour, int bgColour) {
        this.font = font;
        this.cellFgColour = colour;
        this.cellBgColour = bgColour;
    }

    public char getBuffer() {
        return buffer;
    }

    public void setBuffer(char buffer) {
        this.buffer = buffer;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }


    public Cell copy(char newBuffer) {
        new Cell(this.font, this.cellFgColour, this.cellBgColour);
        this.buffer = newBuffer;
        return this;
    }

    public void reset() {
        buffer = ' ';
        this.font = 0;
        this.cellFgColour = 0;
        this.cellBgColour = 0;
    }


}
