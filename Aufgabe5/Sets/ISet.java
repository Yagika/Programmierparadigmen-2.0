package Sets;

import interfaces.OrdSet;
import interfaces.Ordered;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * OrdSet implementation where before(x,y) returns an iterator
 * over all elements z with x < z < y.
 *
 * @param <E> element type
 */
public class ISet<E> extends AbstractOrdSet<E, Iterator<E>> {

    /**
     * Temporary list node used to store the result of before(x, y).
     */
    private static final class TempNode<E> {
        final E elem;
        TempNode<E> next;

        TempNode(E elem, TempNode<E> next) {
            this.elem = elem;
            this.next = next;
        }
    }

    /**
     * Iterator over the temporary list of elements between x and y.
     */
    private static final class BetweenIterator<E> implements Iterator<E> {

        private TempNode<E> current;

        BetweenIterator(TempNode<E> head) {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            E value = current.elem;
            current = current.next;
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Creates a new ISet with the given constraint object.
     * The constraint may be {@code null} (no additional restrictions).
     */
    public ISet(Ordered<? super E, ?> c) {
        super(c);
    }

    @Override
    public Iterator<E> before(E x, E y) {
        ElementNode<E> nx = findNode(x);
        ElementNode<E> ny = findNode(y);

        // If either element is not in the set, or x is not before y: return null.
        if (nx == null || ny == null || !isReachable(nx, ny)) {
            return null;
        }

        // Collect all elements z such that x <* z <* y
        TempNode<E> head = null;
        TempNode<E> tail = null;

        for (ElementNode<E> n = elementsHead; n != null; n = n.next) {
            if (n == nx || n == ny) {
                continue;
            }
            if (isReachable(nx, n) && isReachable(n, ny)) {
                TempNode<E> t = new TempNode<>(n.elem, null);
                if (head == null) {
                    head = t;
                    tail = t;
                } else {
                    tail.next = t;
                    tail = t;
                }
            }
        }

        return new BetweenIterator<>(head);
    }

    @Override
    public void setBefore(E x, E y) {
        if (x == y) {
            throw new IllegalArgumentException("x and y must be different");
        }

        // Check constraint: either no constraint or c.before(x,y) != null
        if (constraint != null) {
            Object r = constraint.before(x, y);
            if (r == null) {
                throw new IllegalArgumentException("Relation not allowed by constraint");
            }
        }

        ElementNode<E> nx = ensureNode(x);
        ElementNode<E> ny = ensureNode(y);

        // Check that there is no path y -> x (to avoid cycles)
        if (isReachable(ny, nx)) {
            throw new IllegalArgumentException("Relation would introduce a cycle");
        }

        // Add the edge if it does not already exist
        if (!hasDirectEdge(nx, ny)) {
            addEdge(nx, ny);
        }
    }
}
