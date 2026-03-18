public class TerminalLine {
    private final Cell[] line;
    private final int width;

    public TerminalLine(int width) {
        line = new Cell[width];
        this.width = width;
        for (int i = 0; i < width; i++) {
            line[i] = new Cell();
        }
    }

    public Cell getCell(int x) {
        return line[x];
    }
    public void clear() {
        for (Cell cell : line) {
            cell.reset();
        }
    }

    public void shiftRight(int fromX) {
        for (int i = line.length - 1; i > fromX; i--) {

            Cell current = line[i];
            Cell leftNeighbor = line[i - 1];

            current.setBuffer(leftNeighbor.getBuffer());
            current.setFont(leftNeighbor.getFont());
            current.setCellBgColour(leftNeighbor.getCellBgColour());
            current.setCellFgColour(leftNeighbor.getCellFgColour());
        }

    }

    public Cell moveRight(int fromX, Cell insertCell) {
        Cell lastCell = line[line.length - 1].copy();

        for(int i = line.length - 1; i > fromX; i--) {
            Cell current = line[i];
            Cell leftNeighbor = line[i - 1];
            current.copyFrom(leftNeighbor);
        }

        line[fromX].copyFrom(insertCell);


        return lastCell;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell cell : line) {
            sb.append(cell.getBuffer());
        }
        return sb.toString();
    }


}
