public class Cursor {
    private int x = 0;
    private int y = 0;
    private final int width;
    private final int height;

    public Cursor(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void set(int x, int y) {
        if (x >= 0 && x < width) this.x = x;
        if (y >= 0 && y < height) this.y = y;
    }

    public void moveNext() {
        x++;
    }

    public void resetX() {
        x = 0;
    }

    public boolean isAtEndOfLine() {
        return x >= width;
    }

    public boolean isAtBottom() {
        return y >= height - 1;
    }

    public void moveDown() {
        if (y < height - 1) y++;
    }
}
