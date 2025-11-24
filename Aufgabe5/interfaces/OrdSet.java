package interfaces;

/**
 * A container with elements of type E and a partial order between them.
 * Inherits Ordered and Iterable.
 *
 * Supports an optional constraint c: new relations x < y may be added
 * only if c.before(x,y) is non-null.
 */
public interface OrdSet<E, R> extends Iterable<E>, Ordered<E, R> {

    /**
     * Sets a new constraint c. Existing relations are checked.
     * If any relation violates c, the old constraint remains and an
     * exception is thrown.
     */
    void check(Ordered<? super E, ?> c);

    /**
     * Sets a new constraint c and removes relations that violate it.
     */
    void checkForced(Ordered<? super E, ?> c);

    /**
     * Returns number of stored elements.
     */
    int size();
}
