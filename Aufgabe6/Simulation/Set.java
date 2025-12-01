package Simulation;

import Meta.Responsible;
import Meta.Precondition;
import Meta.Postcondition;


/**
 * Simple set-like structure without generics and without arrays.
 * Implemented as singly linked list.
 */
@Responsible("Yana")
public class Set {

    private static class Node {
        Object value;
        Node next;

        Node(Object value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node head;
    private int size;

    public Set() {
        head = null;
        size = 0;
    }
    @Postcondition("If the argument is not null, the element is added and size() is increased by 1.")
    public void add(Object o) {
        if (o == null) return;
        head = new Node(o, head);
        size++;
    }

    public int size() {
        return size;
    }

    /**
     * Returns the i-th element (0-based).
     */
    @Precondition("0 <= index < size()")
    public Object get(int index) {
        Node cur = head;
        int i = 0;
        while (cur != null && i < index) {
            cur = cur.next;
            i++;
        }
        return cur != null ? cur.value : null;
    }

    @Postcondition("All elements identical to the argument by '==' are removed, and size() is decreased accordingly.")
    public void remove(Object o) {
        Node dummy = new Node(null, head);
        Node prev = dummy;
        Node cur = head;
        while (cur != null) {
            if (cur.value == o) {
                prev.next = cur.next;
                size--;
            } else {
                prev = cur;
            }
            cur = cur.next;
        }
        head = dummy.next;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
