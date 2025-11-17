package Architecture.Iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Einfacher Iterator über genau ein Element.
 * Wird verwendet, um Iteratoren zurückzugeben, die this als einziges Element enthalten.
 */
public class SingleElementIterator<T> implements Iterator<T> {

    private T element;
    private boolean consumed;

    public SingleElementIterator(T element) {
        this.element = element;
        // Wenn element null ist, ist der Iterator sofort leer.
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
