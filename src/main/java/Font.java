public class Font {
    private int font = 0;
    private int bg = 0;
    private int fg = 0;

    public void update(int font, int bg, int fg) {
        this.font = font;
        this.bg = bg;
        this.fg = fg;
    }

    public void applyTo(Cell cell) {
        cell.setFont(font);
        cell.setCellBgColour(bg);
        cell.setCellFgColour(fg);
    }
}