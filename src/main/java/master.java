import java.util.Scanner;

public class master {
    public static void main(String[] args) { // Pamiętaj o 'public static' i 'String[] args'!
        // 1. Inicjalizacja
        baseTreminal buffer = new baseTreminal(24, 80, 100);
        TerminalRender renderer = new TerminalRender(buffer);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Terminal uruchomiony. Wpisz coś i naciśnij Enter (wpisz 'exit' by wyjść):");

        // 2. Pętla interakcyjna
        while (true) {
            // Rysujemy aktualny stan
            renderer.render();

            // Czekamy na tekst od użytkownika
            System.out.print("> ");
            String input = scanner.nextLine();

            // Wyjście z programu
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            // Przekazujemy tekst do bufora (dodajemy \n, bo nextLine go zabiera)
            buffer.writeInput(input + "\n");
        }

        System.out.println("Terminal zamknięty.");
    }
}