package Sets;

import interfaces.Modifiable;
import interfaces.Ordered;

/**
 * OrdSet whose elements are Modifiable.
 * before(x,y) behaves like in OSet and returns a Slice.
 * plus/minus create new elements via add/subtract and relate
 * them before the original elements (if different).
 */
public class MSet<E extends Modifiable<X, E>, X>
        extends AbstractOrdSet<E, OSet.Slice<E>> {

    public MSet(Ordered<? super E, ?> c) {
        super(c);
    }

    @Override
    public OSet.Slice<E> before(E x, E y) {
        ElementNode<E> nx = findNode(x);
        ElementNode<E> ny = findNode(y);

        if (nx == null || ny == null || !isReachable(nx, ny)) {
            return null;
        }

        OSet.Slice<E> slice = new OSet.Slice<>();

        // collect all elements z with x <* z <* y
        for (ElementNode<E> n = elementsHead; n != null; n = n.next) {
            if (n == nx || n == ny) {
                continue;
            }
            if (isReachable(nx, n) && isReachable(n, ny)) {
                slice.add(n.elem);
            }
        }

        // reconstruct order relations between elements in the slice
        for (ElementNode<E> a = elementsHead; a != null; a = a.next) {
            if (!slice.contains(a.elem)) {
                continue;
            }
            for (ElementNode<E> b = elementsHead; b != null; b = b.next) {
                if (a == b || !slice.contains(b.elem)) {
                    continue;
                }
                if (isReachable(a, b)) {
                    slice.setBefore(a.elem, b.elem);
                }
            }
        }

        return slice;
    }

    @Override
    public void setBefore(E x, E y) {
        if (x == y) {
            throw new IllegalArgumentException("x and y must be different");
        }
        if (constraint != null) {
            Object r = constraint.before(x, y);
            if (r == null) {
                throw new IllegalArgumentException("Relation not allowed by constraint");
            }
        }
        ElementNode<E> nx = ensureNode(x);
        ElementNode<E> ny = ensureNode(y);
        if (isReachable(ny, nx)) {
            throw new IllegalArgumentException("Relation would introduce a cycle");
        }
        if (!hasDirectEdge(nx, ny)) {
            addEdge(nx, ny);
        }
    }

    /**
     * For each element e in this set, performs:
     * setBefore(e.add(x), e), if add(x) returns a different object.
     */
    public void plus(X x) {
        for (E e : this) {
            E modified = e.add(x);
            if (modified != e) {          // only if extension was possible
                setBefore(modified, e);
            }
        }
    }

    /**
     * For each element e in this set, performs:
     * setBefore(e.subtract(x), e), if subtract(x) returns a different object.
     */
    public void minus(X x) {
        for (E e : this) {
            E modified = e.subtract(x);
            if (modified != e) {          // only if removal was possible
                setBefore(modified, e);
            }
        }
    }
}
