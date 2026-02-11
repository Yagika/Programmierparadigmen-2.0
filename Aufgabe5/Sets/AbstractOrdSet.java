package Sets;

import interfaces.OrdSet;
import interfaces.Ordered;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Base implementation for OrdSet.
 * Stores elements in a linked list and order relations as a DAG.
 * Provides constraint handling, reachability checks and an iterator.
 *
 * @param <E> element type
 * @param <R> result type of before(E,E)
 */
abstract class AbstractOrdSet<E, R> implements OrdSet<E, R> {

    /**
     * One element in the internal linked list.
     * Equality is based on object identity (==).
     */
    protected static final class ElementNode<E> {
        final E elem;
        ElementNode<E> next;

        ElementNode(E elem, ElementNode<E> next) {
            this.elem = elem;
            this.next = next;
        }
    }

    /**
     * Directed edge from one element to another.
     */
    protected static final class Edge<E> {
        final ElementNode<E> from;
        final ElementNode<E> to;
        Edge<E> next;

        Edge(ElementNode<E> from, ElementNode<E> to, Edge<E> next) {
            this.from = from;
            this.to = to;
            this.next = next;
        }
    }

    /**
     * Stack node for DFS (array-free).
     */
    private static final class NodeStack<E> {
        final ElementNode<E> node;
        NodeStack<E> next;

        NodeStack(ElementNode<E> node, NodeStack<E> next) {
            this.node = node;
            this.next = next;
        }
    }

    /**
     * Iterator over the linked list of elements.
     */
    private final class ElementIterator implements Iterator<E> {

        private ElementNode<E> current = elementsHead;

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

    /** Head of the linked list of all elements. */
    protected ElementNode<E> elementsHead;

    /** Head of the linked list of all order relations (edges). */
    protected Edge<E> edgesHead;

    /** Number of distinct elements in the container. */
    protected int size;

    /** Current constraint for allowed order relations, may be {@code null}. */
    protected Ordered<? super E, ?> constraint;

    protected AbstractOrdSet(Ordered<? super E, ?> c) {
        this.constraint = c;
        this.elementsHead = null;
        this.edgesHead = null;
        this.size = 0;
    }

    /**
     * Returns the node storing the given element or {@code null} if not found.
     * Equality is by object identity, not by {@code equals}.
     */
    protected ElementNode<E> findNode(E e) {
        for (ElementNode<E> n = elementsHead; n != null; n = n.next) {
            if (n.elem == e) {
                return n;
            }
        }
        return null;
    }

    /**
     * Ensures that the given element is contained in this set.
     * If already present, returns the existing node; otherwise,
     * inserts a new node at the front of the list.
     */
    protected ElementNode<E> ensureNode(E e) {
        ElementNode<E> node = findNode(e);
        if (node != null) {
            return node;
        }
        elementsHead = new ElementNode<>(e, elementsHead);
        size++;
        return elementsHead;
    }

    /**
     * Returns true if there is a path from {@code from} to {@code to}
     * following the current order relations.
     */
    protected boolean isReachable(ElementNode<E> from, ElementNode<E> to) {
        if (from == null || to == null) {
            return false;
        }
        // Simple DFS without visited set; we rely on the invariant that
        // the graph is acyclic, so we cannot loop forever.
        NodeStack<E> stack = new NodeStack<>(from, null);

        while (stack != null) {
            ElementNode<E> current = stack.node;
            stack = stack.next;
            if (current == to) {
                return true;
            }
            // push all successors current -> ?
            for (Edge<E> e = edgesHead; e != null; e = e.next) {
                if (e.from == current) {
                    stack = new NodeStack<>(e.to, stack);
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there is a direct edge from {@code from} to {@code to}.
     */
    protected boolean hasDirectEdge(ElementNode<E> from, ElementNode<E> to) {
        for (Edge<E> e = edgesHead; e != null; e = e.next) {
            if (e.from == from && e.to == to) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new order relation {@code from -> to}. Does not check for cycles
     * or constraints and does not check for duplicates.
     */
    protected void addEdge(ElementNode<E> from, ElementNode<E> to) {
        edgesHead = new Edge<>(from, to, edgesHead);
    }

    @Override
    public void check(Ordered<? super E, ?> c) {
        if (c == null) {
            // Always allowed: no additional restrictions.
            this.constraint = null;
            return;
        }
        // Verify all existing edges (a,b) using c.before(a,b).
        for (Edge<E> e = edgesHead; e != null; e = e.next) {
            Object r = c.before(e.from.elem, e.to.elem);
            if (r == null) {
                // Violation: keep old constraint and throw.
                throw new IllegalArgumentException("Existing relation not allowed by new constraint");
            }
        }
        // All relations allowed -> set new constraint
        this.constraint = c;
    }

    @Override
    public void checkForced(Ordered<? super E, ?> c) {
        this.constraint = c;
        if (c == null) {
            // No restriction: keep everything.
            return;
        }
        // Remove all edges not allowed by c.
        Edge<E> prev = null;
        Edge<E> current = edgesHead;
        while (current != null) {
            Object r = c.before(current.from.elem, current.to.elem);
            if (r == null) {
                // Remove current edge
                if (prev == null) {
                    edgesHead = current.next;
                } else {
                    prev.next = current.next;
                }
                current = (prev == null) ? edgesHead : prev.next;
            } else {
                prev = current;
                current = current.next;
            }
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ElementIterator();
    }

    @Override
    public int size() {
        return size;
    }
}
