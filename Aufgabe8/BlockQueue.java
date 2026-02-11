import java.util.*;

/**
 * Minimal synchronized work-queue for blocks.
 *
 * Consumers exit when queue is empty AND closed.
 */
public final class BlockQueue<T> {
    private final ArrayDeque<T> q = new ArrayDeque<>();
    private boolean closed = false;

    public synchronized void addAll(List<T> items) {
        if (closed) throw new IllegalStateException("Queue already closed");
        for (T it : items) q.addLast(it);
        notifyAll();
    }

    /** Signals: no more items will be added. */
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    /**
     * Takes next item or returns null if queue is drained + closed.
     * @throws InterruptedException if thread is interrupted while waiting
     */
    public synchronized T take() throws InterruptedException {
        while (q.isEmpty() && !closed) {
            wait();
        }
        return q.pollFirst(); // null when empty+closed
    }
}
