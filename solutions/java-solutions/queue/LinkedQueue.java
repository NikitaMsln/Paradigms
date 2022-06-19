package queue;

import java.util.Objects;

/**
 * This: linked mode <br/>
 * <h3> Class: {@link queue.LinkedQueue} extends {@link queue.AbstractQueue} implements {@link queue.Queue} </h3>
 */
public class LinkedQueue  extends AbstractQueue {
    private Node head, tail;

    @Override
    protected void init() {
        head = null;
        tail = null;
    }

    @Override
    protected void pushBack(Object element, int size) {
        final Node node = new Node(element);
        if (size > 0) {
            tail.next = node;
        } else {
            head = node;
        }
        tail = node;
    }

    @Override
    protected void popFront() {
        head = head.next;
    }

    @Override
    protected Object elementImpl() {
        return head.element;
    }

    private static class Node {
        private final Object element;
        private Node next = null;

        private Node(Object element) {
            this.element = Objects.requireNonNull(element);
        }
    }
}
