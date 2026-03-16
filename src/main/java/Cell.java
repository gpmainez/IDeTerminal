public class Cell {
    private char buffer = ' ';
    private int font = 0;
    private int colour = 0;
    private int bgColour = 0;

    public Cell() {
    }

    public Cell(int font, int colour, int bgColour) {
        this.font = font;
        this.colour = colour;
        this.bgColour = bgColour;
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
        new Cell(this.font, this.colour, this.bgColour);
        this.buffer = newBuffer;
        return this;
    }

    public void reset() {
        buffer = ' ';
        this.font = 0;
        this.colour = 0;
        this.bgColour = 0;
    }


}
