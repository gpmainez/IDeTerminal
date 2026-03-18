package Terminal;

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

    public void moveRight() {
        moveRight(1);
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
        moveDown(1);
    }


    public void moveUp(int n) {
        y = Math.max(0, y - n);
    }

    public void moveDown(int n) {
        y = Math.min(height - 1, y + n);
    }

    public void moveLeft(int n) {
        x = Math.max(0, x - n);
    }

    public void moveRight(int n) {
        x = Math.min(width, x + n);
    }
}
