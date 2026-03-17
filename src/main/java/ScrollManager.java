import java.util.ArrayDeque;
import java.util.Deque;

public class ScrollManager {
    private final Deque<TerminalLine> history = new ArrayDeque<>();
    private final int maxSize;

    public ScrollManager(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(TerminalLine line) {
        if (history.size() >= maxSize) {
            history.removeFirst();
        }
        history.addLast(line);
    }

    public Deque<TerminalLine> getHistory() { return history; }
}