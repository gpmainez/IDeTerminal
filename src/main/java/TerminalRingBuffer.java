public class TerminalRingBuffer implements TerminalBuffer {
    private final TerminalLine[] buffer;
    private final int totalCapacity;

    private int width;
    private int height;

    private int topVisibleLine = 0;
    private int currentX = 0;
    private int currentY = 0;

    int tail;
    int head;
    int bufferSize;

    int font;
    int screenBgColour;
    int screenFgColour;




    public TerminalRingBuffer(int height, int width, int maxScroll) {
        this.height = height;
        this.width = width;
        this.totalCapacity = height + maxScroll;
        this.buffer = new TerminalLine[totalCapacity];

        for (int i = 0; i < totalCapacity; i++) {
            buffer[i] = new TerminalLine(width);
        }

        this.tail = height - 1;
        this.bufferSize = height;
        this.head = 0;
    }

    public void nextLine() {
        currentX = 0;

        if (currentY < height - 1) {
            currentY++;

        } else {
            tail = (tail + 1) % totalCapacity;

            if (bufferSize < totalCapacity) {
                bufferSize++;
            } else {
                head = (head + 1) % totalCapacity;

            }
            buffer[tail].clear();

        }
    }
    @Override
    public void setAttribute(int form, int bg, int fg) {

    }

    @Override
    public void writeInput(String input) {
        for (int i = 0; i <input.length() ; i++ ) {
            char c = input.charAt(i);


            if (c == '\n') {
                nextLine();
                continue;
            }

            if (currentX >= width) {
                nextLine();
            }
            TerminalLine currentLine = buffer[getCursorIdx(currentY)];
            if (c == '\t') {
                for (int j = 0; j < 4; j++) {
                    if (currentX >= width) {
                        nextLine();
                        currentLine = buffer[getCursorIdx(currentY)];
                    }
                    currentLine.getCell(currentX).setBuffer(' ');
                    currentX++;
                }
                continue;
            }

            Cell cell = currentLine.getCell(currentX);
            cell.setBuffer(c);
            cell.setFont(this.font);
            cell.setCellBgColour(this.screenBgColour);
            cell.setCellFgColour(this.screenFgColour);



            currentX++;
        }

    }

    private int getCursorIdx(int y) {
        return (tail - (height - 1 - y) + totalCapacity) % totalCapacity;
    }

    @Override
    public char getCharAt(int x, int y) {
        if (y < 0 || y >= height || x < 0 || x >= width) return ' ';
        return buffer[getCursorIdx(y)].getCell(x).getBuffer();
    }

    @Override
    public int getAttributesAt(int x, int y) {
        return 0;
    }

    @Override
    public int getCursorX() {
        return currentX;
    }

    @Override
    public int getCursorY() {
        return currentY;
    }

    @Override
    public void setCursor(int x, int y) {
        this.currentX = Math.max(0, Math.min(x, width - 1));
        this.currentY = Math.max(0, Math.min(y, height - 1));

    }

    @Override
    public void insert(String text) {

    }
}
