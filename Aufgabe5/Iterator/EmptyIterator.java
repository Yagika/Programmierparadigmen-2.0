package Iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An always-empty iterator.
 * <p>
 * Implemented as a singleton, because it is stateless and can be reused.
 */
public class EmptyIterator<T> implements Iterator<T> {

    @SuppressWarnings("rawtypes")
    private static final EmptyIterator INSTANCE = new EmptyIterator<>();


    private EmptyIterator() {
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
