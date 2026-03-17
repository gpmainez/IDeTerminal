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

    public void shiftRight(int fromX) {
        // Idziemy od końca linii do miejsca wstawienia
        for (int i = line.length - 1; i > fromX; i--) {
            // Kopiujemy znak i styl z sąsiada po lewej
            Cell current = line[i];
            Cell leftNeighbor = line[i - 1];

            current.setBuffer(leftNeighbor.getBuffer());
            current.setFont(leftNeighbor.getFont());
            current.setCellBgColour(leftNeighbor.getCellBgColour());
            current.setCellFgColour(leftNeighbor.getCellFgColour());
        }
        // Po przesunięciu, na miejscu fromX zostaje "stary" znak,
        // który zaraz nadpiszemy w metodzie insert.
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
