public class TerminalRender {

    private final TerminalBuffer buffer;

    public TerminalRender(TerminalBuffer buffer) {
        this.buffer = buffer;
    }

    public void render() {
        // Próba czyszczenia (działa w prawdziwych terminalach)
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Rysujemy górną krawędź "monitora"
        System.out.println("+" + "-".repeat(80) + "+");

        for (int y = 0; y < 24; y++) {
            System.out.print("|"); // Lewa ściana

            for (int x = 0; x < 80; x++) {
                System.out.print(buffer.getCharAt(x, y));
            }

            System.out.println("|"); // Prawa ściana
        }

        // Dolna krawędź "monitora"
        System.out.println("+" + "-".repeat(80) + "+");
        System.out.println("Kursor: [" + buffer.getCursorX() + ", " + buffer.getCursorY() + "]");
    }
}

