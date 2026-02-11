package Sets;

import interfaces.Modifiable;
import interfaces.Ordered;

/**
 * OrdSet where before(x,y) returns a Slice containing all
 * elements z with x < z < y and their induced order.
 *
 * @param <E> element type
 */
public class OSet<E> extends AbstractOrdSet<E, OSet.Slice<E>> {

    /**
     * A small independent partial-order container.
     * Implements Ordered<E,Boolean> and Modifiable<E,Slice<E>>.
     */
    public static class Slice<E> implements Ordered<E, Boolean>, Modifiable<E, Slice<E>> {

        /** Node in the element list of the slice. */
        private static final class SElementNode<E> {
            final E elem;
            SElementNode<E> next;

            SElementNode(E elem, SElementNode<E> next) {
                this.elem = elem;
                this.next = next;
            }
        }

        /** Edge in the order graph of the slice: from -> to. */
        private static final class SEdge<E> {
            final SElementNode<E> from;
            final SElementNode<E> to;
            SEdge<E> next;

            SEdge(SElementNode<E> from, SElementNode<E> to, SEdge<E> next) {
                this.from = from;
                this.to = to;
                this.next = next;
            }
        }

        /** Simple stack for DFS without arrays. */
        private static final class SNodeStack<E> {
            final SElementNode<E> node;
            SNodeStack<E> next;

            SNodeStack(SElementNode<E> node, SNodeStack<E> next) {
                this.node = node;
                this.next = next;
            }
        }

        private SElementNode<E> elementsHead;
        private SElementNode<E> elementsTail;
        private SEdge<E> edgesHead;

        public Slice() {
            this.elementsHead = null;
            this.elementsTail = null;
            this.edgesHead = null;
        }

        private SElementNode<E> findNode(E e) {
            for (SElementNode<E> n = elementsHead; n != null; n = n.next) {
                if (n.elem == e) { // identity
                    return n;
                }
            }
            return null;
        }

        private boolean isReachable(SElementNode<E> from, SElementNode<E> to) {
            if (from == null || to == null) {
                return false;
            }
            SNodeStack<E> stack = new SNodeStack<>(from, null);
            while (stack != null) {
                SElementNode<E> cur = stack.node;
                stack = stack.next;
                if (cur == to) {
                    return true;
                }
                for (SEdge<E> e = edgesHead; e != null; e = e.next) {
                    if (e.from == cur) {
                        stack = new SNodeStack<>(e.to, stack);
                    }
                }
            }
            return false;
        }

        private boolean hasDirectEdge(SElementNode<E> from, SElementNode<E> to) {
            for (SEdge<E> e = edgesHead; e != null; e = e.next) {
                if (e.from == from && e.to == to) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns true if this slice contains the given element (by identity).
         */
        public boolean contains(E e) {
            return findNode(e) != null;
        }

        @Override
        public Boolean before(E x, E y) {
            SElementNode<E> nx = findNode(x);
            SElementNode<E> ny = findNode(y);
            if (nx == null || ny == null) {
                return null;
            }
            return isReachable(nx, ny) ? Boolean.TRUE : null;
        }

        @Override
        public void setBefore(E x, E y) {
            SElementNode<E> nx = findNode(x);
            SElementNode<E> ny = findNode(y);
            if (nx == null || ny == null) {
                throw new IllegalArgumentException("Both elements must already be contained");
            }
            if (nx == ny) {
                throw new IllegalArgumentException("x and y must be different");
            }
            if (isReachable(ny, nx)) {
                throw new IllegalArgumentException("Relation would create a cycle");
            }
            if (!hasDirectEdge(nx, ny)) {
                edgesHead = new SEdge<>(nx, ny, edgesHead);
            }
        }

        @Override
        public Slice<E> add(E e) {
            // only add if not present
            if (findNode(e) != null) {
                return this;
            }
            SElementNode<E> node = new SElementNode<>(e, null);
            if (elementsHead == null) {
                elementsHead = node;
                elementsTail = node;
            } else {
                elementsTail.next = node;
                elementsTail = node;
            }
            return this;
        }

        @Override
        public Slice<E> subtract(E e) {
            SElementNode<E> prev = null;
            SElementNode<E> cur = elementsHead;
            while (cur != null && cur.elem != e) {
                prev = cur;
                cur = cur.next;
            }
            if (cur == null) {
                return this; // not contained
            }

            // remove from element list
            if (prev == null) {
                elementsHead = cur.next;
            } else {
                prev.next = cur.next;
            }
            if (elementsTail == cur) {
                elementsTail = prev;
            }

            // remove all edges touching this node
            SEdge<E> ePrev = null;
            SEdge<E> eCur = edgesHead;
            while (eCur != null) {
                if (eCur.from == cur || eCur.to == cur) {
                    if (ePrev == null) {
                        edgesHead = eCur.next;
                    } else {
                        ePrev.next = eCur.next;
                    }
                    eCur = (ePrev == null) ? edgesHead : ePrev.next;
                } else {
                    ePrev = eCur;
                    eCur = eCur.next;
                }
            }
            return this;
        }
    }

    public OSet(Ordered<? super E, ?> c) {
        super(c);
    }

    @Override
    public Slice<E> before(E x, E y) {
        // Here we use AbstractOrdSet.ElementNode<E>, not Slice.SElementNode
        ElementNode<E> nx = findNode(x);
        ElementNode<E> ny = findNode(y);

        // x or y not in set, or x not before y
        if (nx == null || ny == null || !isReachable(nx, ny)) {
            return null;
        }

        Slice<E> slice = new Slice<>();

        // add all elements z with x <* z <* y, in this OSet's order
        for (ElementNode<E> n = elementsHead; n != null; n = n.next) {
            if (n == nx || n == ny) {
                continue;
            }
            if (isReachable(nx, n) && isReachable(n, ny)) {
                slice.add(n.elem);
            }
        }

        // reconstruct order relations inside the slice
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
}
