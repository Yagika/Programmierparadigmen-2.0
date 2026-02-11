package interfaces;

/**
 * Describes a partial order on elements of type E.
 * The method before(x,y) returns a non-null value if x < y.
 *
 * R – result type returned by before
 */
public interface Ordered<E, R> {

    /**
     * Returns non-null if x is before y according to this order.
     * Must not modify this, x, or y.
     */
    R before(E x, E y);

    /**
     * Modifies this order so that x is before y.
     * May throw IllegalArgumentException if the relation is illegal.
     */
    void setBefore(E x, E y);
}
