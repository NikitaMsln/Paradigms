package queue;

import java.util.Objects;

/**
 * This: one class instance
 * <h3> Classes {@link queue.ArrayQueueModule}, {@link queue.ArrayQueueADT}, {@link queue.ArrayQueue}</h3>
 * Model: {@code a[0 .. size - 1], first in (in tail) -- first out (from head)}
 * <br/>
 * Invariant: {@code size >= 0 && for i in [0, size): a[i] != null && a[0] -- queue head && a[size - 1] -- queue tail}
 * (tail -> a[size - 1], head -> a[0])
 * <br/>
 * Let {@code saveOrder(array1, head1, array2, head2, cnt): for i in [0, cnt) array1[head1 + i] == array2[head2 + i]}
 * <br/>
 * Let {@code immutable(n) = saveOrder(a, 0, a', 0, min(size, size'))}
 */
public class ArrayQueueModule {
    private static Object[] elements = new Object[2];
    private static int head = 0, size = 0;

    /**
     * <h3>Function {@link queue.ArrayQueueModule#enqueue(Object)}</h3>
     * Pred: {@code element != null}
     * <br>
     * Post: {@code size' == size + 1 && a[size'] == element && immutable(size)}
     * @param element not null object
     * @throws NullPointerException if element is null
     */
    public static void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        int tail = (head + size) % elements.length;
        elements[tail] = element;
        size++;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#ensureCapacity(int)}</h3>
     * @param capacity integer
     */
    private static void ensureCapacity(final int capacity) {
        if (elements.length < capacity) {
            Object[] newElements = new Object[capacity * 2];
            System.arraycopy(elements, head, newElements, 0, elements.length - head);
            System.arraycopy(elements, 0, newElements, elements.length - head, head);
            elements = newElements;
            head = 0;
        }
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#element()}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[0] && immutable(size) && size' == size}
     * @return R -- not null object
     * @throws AssertionError if size == 0
     */
    public static Object element() {
        assert size >= 1;
        return elements[head];
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#dequeue()}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[0] && size' == size - 1 && saveOrder(a, 1, a', 0, size')}
     * @return R -- not null object
     * @throws AssertionError if size == 0
     */
    public static Object dequeue() {
        assert size >= 1;
        Object element = elements[head];
        elements[head] = null;
        size--;
        if (++head == elements.length) {
            head = 0;
        }
        return element;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#size()}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code R == size && size' == size && immutable(size) }
     * @return R
     */
    public static int size() {
        return size;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#isEmpty()}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code R == (size == 0) && immutable(size) && size' == size }
     * @return R
     */
    public static boolean isEmpty() {
        return size == 0;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#clear()}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code size' == 0 }
     */
    public static void clear() {
        size = 0;
        head = 0;
        elements = new Object[2];
    }
}
