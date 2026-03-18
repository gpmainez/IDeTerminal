public class TerminalRingBuffer implements TerminalBuffer {
    private final TerminalLine[] buffer;
    private final int width;
    private final int height;
    private final int totalCapacity;

    private int head = 0;
    private int tail;
    private int bufferSize;


    private final Cursor cursor;

    private int currentFont = 0;
    private int currentBg = 0;
    private int currentFg = 0;

    public TerminalRingBuffer(int height, int width, int maxScroll) {
        this.height = height;
        this.width = width;
        this.totalCapacity = height + maxScroll;
        this.buffer = new TerminalLine[totalCapacity];

        for (int i = 0; i < totalCapacity; i++) {
            buffer[i] = new TerminalLine(width);
        }

        this.cursor = new Cursor(width, height);
        this.tail = height - 1;
        this.bufferSize = height;
    }

    @Override
    public void setAttribute(int font, int bg, int fg) {
        this.currentFont = font;
        this.currentBg = bg;
        this.currentFg = fg;
    }
    //method to move cursor to the nextLine and also manage history overwrite
    public void nextLine() {
        cursor.resetX();

        if (cursor.isAtBottom()) {
            tail = (tail + 1) % totalCapacity;
            if (bufferSize < totalCapacity) {
                bufferSize++;
            } else { //the history buffer is full at this moment we start overwrite
                head = (head + 1) % totalCapacity;
            }

            buffer[tail].clear();
        } else {
            cursor.moveDown();
        }
    }
    //physically my cursorY index is always between 0 and height -1
    //but logically it s going down to totalCapacity so we have to manage it
    private int getCursorIdx(int logicalY) {
        int distanceFromBottom = (height - 1) - logicalY;
        int idx = (tail - distanceFromBottom) % totalCapacity;

        if (idx < 0) {
            idx += totalCapacity;
        }
        return idx;
    }

    @Override public int getCursorX() { return cursor.getX(); }
    @Override public int getCursorY() { return cursor.getY(); }
    @Override public void setCursor(int x, int y) { cursor.set(x, y); }

    @Override
    public void writeInput(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '\n') {
                nextLine();
                continue;
            }

            if (cursor.isAtEndOfLine()) {
                nextLine();
            }

            TerminalLine currentLine = buffer[getCursorIdx(cursor.getY())];

            if (c == '\t') {
                for (int j = 0; j < 4; j++) {
                    if (cursor.isAtEndOfLine()) {
                        nextLine();
                        currentLine = buffer[getCursorIdx(cursor.getY())];
                    }

                    Cell cell = currentLine.getCell(cursor.getX());
                    cell.setCell(' ',currentFont,currentBg,currentFg);
                    cursor.moveNext();
                }
                continue;
            }


            Cell cell = currentLine.getCell(cursor.getX());
            cell.setCell(c,currentFont,currentBg,currentFg);

            cursor.moveNext();
        }
    }

    @Override
    public char getCharAt(int x, int y) {
        if (x < 0 || x >= width) {
            return ' '; //return emptybuffor null would result in NullPointerException
        }

        int availableScrollback = bufferSize - height;
        if (y < -availableScrollback || y >= height) {
            return ' ';
        }

        int physicalIdx = getCursorIdx(y);
        return buffer[physicalIdx].getCell(x).getBuffer();
    }

    @Override
    public int getAttributesAt(int x, int y) {
        if (x < 0 || x >= width) return 0;

        int availableScrollback = bufferSize - height;
        if (y < -availableScrollback || y >= height) return 0;

        int physicalIdx = getCursorIdx(y);
        return buffer[physicalIdx].getCell(x).getFont();
    }

    @Override
    public void insert(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                nextLine();
                continue;
            }

            if (cursor.isAtEndOfLine()) {
                nextLine();
            }

            Cell fallingBlock = new Cell(c, currentFont, currentFg, currentBg);

            int targetX = cursor.getX();
            int targetY = cursor.getY();

            while (fallingBlock != null && !fallingBlock.isEmpty()) {
                if (targetY >= height) {
                    nextLine();
                    targetY = height - 1;
                }

                TerminalLine currentLine = buffer[getCursorIdx(targetY)];

                fallingBlock = currentLine.moveRight(targetX, fallingBlock);
                targetY++;
                targetX = 0;
            }

            cursor.moveNext();
        }
    }



}