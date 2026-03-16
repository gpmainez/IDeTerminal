public class TerminalLine {
    private final Cell[] line;

    public TerminalLine(int width) {
        line = new Cell[width];

        for (int i = 0; i < width; i++) {
            line[i] = new Cell();
        }
    }


    public void clear() {
        for (Cell cell : line) {
            cell.reset();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell cell : line) {
            sb.append(cell.getBuffer());
        }
        return sb.toString();
    }

    public Cell getCell(int x) {
        return line[x];
    }
}
