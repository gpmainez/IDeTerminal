import java.util.Deque;
import java.util.List;

public class baseTreminal implements TerminalBuffer {
    private List<TerminalBuffer> screen;
    private Deque<TerminalBuffer> stack;

    private final int screenHeigth;
    private final int screenWidth;
    private final int maxScreenStack;


    int currentX = 0;
    int currentY = 0;

    int screenFgColour = 0;
    int screenBgColour = 0;
    int font = 0;

    @Override
    public void setAttribute(int font, int bg, int fg) {
        this.screenFgColour = fg;
        this.screenBgColour = bg;
        this.font = font;
    }

    @Override
    public void writeInput(String input) {
        for(char c : input.toCharArray()) {
            placeCell(c);
        }


    }

    public void nextLine() {
        currentX = 0;
        currentY = 0;
        screenFgColour = 0;
        new TerminalLine(screenWidth);
    }


    public void placeCell(char c) {

            if(c == '\n') {
                nextLine();
                return;
            }

            if(currentX >= screenWidth) {
                nextLine();
            }

            TerminalLine currentLine = screen.get(currentY);



    }
}
