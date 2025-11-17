package Architecture.Iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Immer leerer Iterator. Singleton-Implementierung.
 */
public class EmptyIterator<T> implements Iterator<T> {

    @SuppressWarnings("rawtypes")
    private static final EmptyIterator INSTANCE = new EmptyIterator<>();

    @SuppressWarnings("unchecked")
    public static <T> EmptyIterator<T> instance() {
        return (EmptyIterator<T>) INSTANCE;
    }

    private EmptyIterator() {
        // Singleton
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
