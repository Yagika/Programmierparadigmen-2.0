package Iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple iterator over exactly one element.
 * <p>
 * Used to return iterators that contain only a single element.
 */
public class SingleElementIterator<T> implements Iterator<T> {

    private T element;
    private boolean consumed;

    /**
     * Creates a new iterator over the given element.
     * If the element is {@code null}, the iterator is empty from the start.
     */
    public SingleElementIterator(T element) {
        this.element = element;
        this.consumed = (element == null);
    }

    @Override
    public boolean hasNext() {
        return !consumed;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        consumed = true;
        return element;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
