package Terminal;

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
    //but logically it s going from 0 to totalCapacity so we have to manage it
    //i take into consideration case when we make a loop in 66 line
    private int getCursorIdx(int logicalY) {
        int distanceFromBottom = (height - 1) - logicalY;
        int idx = (tail - distanceFromBottom) % totalCapacity;

        if (idx < 0) {
            idx += totalCapacity;
        }
        return idx;
    }

    private boolean isWideChar(char c) {
        return (c >= '\u1100' && c <= '\u115F') ||
                (c >= '\u2E80' && c <= '\uA4CF') ||
                (c >= '\uAC00' && c <= '\uD7A3') ||
                (c >= '\uF900' && c <= '\uFAFF') ||
                (c >= '\uFE10' && c <= '\uFE19') ||
                (c >= '\uFF00' && c <= '\uFF60') ||
                (c >= '\uFFE0' && c <= '\uFFE6');
    }

    @Override
    public int getCursorX() {
        return cursor.getX();
    }

    @Override
    public int getCursorY() {
        return cursor.getY();
    }

    @Override
    public void setCursor(int x, int y) {
        //the logic to ingore is in cursor
        cursor.set(x, y);
    }

    @Override
    public void writeInput(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);


            if (c == '\n') {
                nextLine();
                continue;
            }

            boolean longChar = isWideChar(c);

//            * have to speracte case in which wideChar is at the end of the line to write it in the nextline
//               guaranteed that i have place for this and just write space at the end of the line

            if (cursor.isAtEndOfLine() || (longChar && cursor.getX() == width - 1)) {
                if (longChar && cursor.getX() == width - 1) {

                    TerminalLine lineBeforeWrap = buffer[getCursorIdx(cursor.getY())];
                    lineBeforeWrap.getCell(cursor.getX()).setCell(' ', currentFont, currentBg, currentFg);
                }
                nextLine();
            }

            TerminalLine currentLine = buffer[getCursorIdx(cursor.getY())];

            if (c == '\t') {
                for (int j = 0; j < 4; j++) {
                    if (cursor.isAtEndOfLine()) {
                        nextLine();
                        currentLine = buffer[getCursorIdx(cursor.getY())];
                    }
                    currentLine.getCell(cursor.getX()).setCell(' ', currentFont, currentBg, currentFg);
                    cursor.moveRight();
                }
                continue;
            }

            Cell cell = currentLine.getCell(cursor.getX());
            cell.setCell(c, currentFont, currentBg, currentFg);
            cursor.moveRight();

//            if (longChar) {
//                Cell emptyCell = currentLine.getCell(cursor.getX());
//                emptyCell.setCell('\0', currentFont, currentBg, currentFg);
//                cursor.moveRight();
//            }
        }
    }

    @Override
    public char getCharAt(int x, int y) {
        if (x < 0 || x >= width) {
            return ' '; //return null would result in NullPointerException
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

            cursor.moveRight();
        }
    }


    @Override
    public void fillLine(char c) {
        TerminalLine currentLine = buffer[getCursorIdx(cursor.getY())];

        for (int x = 0; x < width; x++) {
            Cell cell = currentLine.getCell(x);
            cell.setCell(c, currentFont, currentFg, currentBg);
        }
    }

    @Override
    public void insertEmptyLine() {
        //basically nextLine method without moving cursor
        tail = (tail + 1) % totalCapacity;
        if (bufferSize < totalCapacity) {
            bufferSize++;
        } else {
            head = (head + 1) % totalCapacity;
        }
        buffer[tail].clear();
    }

    //clear the entire screen
    @Override
    public void clearScreen() {
        for (int y = 0; y < height; y++) {
            buffer[getCursorIdx(y)].clear();
        }
    }

    @Override
    public void clearAll() {
        for (int i = 0; i < totalCapacity; i++) {
            buffer[i].clear();
        }

        tail = height - 1;
        head = 0;
        bufferSize = height;
        cursor.set(0, 0);
    }

    @Override
    public String line(int y) {
        //how long is the "history" in buffer
        int availableScrollback = bufferSize - height;
        if (y < -availableScrollback || y >= height) {
            return "";
        }

        int physicalIdx = getCursorIdx(y);
        return buffer[physicalIdx].toString();
    }

    @Override
    public String currentScreen() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            sb.append(line(y));
            if (y < height - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    @Override
    public String historyBuffer() {
        StringBuilder sb = new StringBuilder();
        int availableScrollback = bufferSize - height;

        for (int y = -availableScrollback; y < height; y++) {
            sb.append(line(y));
            if (y < height - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }


}