package queue;

import java.util.Objects;

/**
 * This: queue as abstract data type
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
public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int head = 0, size = 0;

    /**
     * <h3>Function {@link queue.ArrayQueueADT#enqueue(ArrayQueueADT, Object)}</h3>
     * Pred: {@code element != null}
     * <br>
     * Post: {@code size' == size + 1 && a[size'] == element && immutable(size)}
     * @param element not null object
     * @throws NullPointerException if element is null
     */
    public static void enqueue(final ArrayQueueADT queue, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, queue.size + 1);
        int tail = (queue.head + queue.size) % queue.elements.length;
        queue.elements[tail] = element;
        queue.size++;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#ensureCapacity(ArrayQueueADT, int)}</h3>
     * @param capacity integer
     */
    private static void ensureCapacity(final ArrayQueueADT queue, final int capacity) {
        if (queue.elements.length < capacity) {
            Object[] newElements = new Object[capacity * 2];
            System.arraycopy(
                    queue.elements, queue.head, newElements, 0, queue.elements.length - queue.head
            );
            System.arraycopy(
                    queue.elements, 0, newElements, queue.elements.length - queue.head, queue.head
            );
            queue.elements = newElements;
            queue.head = 0;
        }
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#element(ArrayQueueADT)}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[0] && immutable(size) && size' == size}
     * @return R -- not null object
     * @throws AssertionError if size == 0
     */
    public static Object element(final ArrayQueueADT queue) {
        assert queue.size >= 1;
        return queue.elements[queue.head];
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#dequeue(ArrayQueueADT)}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[0] && size' == size - 1 && saveOrder(a, 1, a', 0, size')}
     * @return R -- not null object
     * @throws AssertionError if size == 0
     */
    public static Object dequeue(final ArrayQueueADT queue) {
        assert queue.size >= 1;
        Object element = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.size--;
        if (++queue.head == queue.elements.length) {
            queue.head = 0;
        }
        return element;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#size(ArrayQueueADT)}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code R == size && size' == size && immutable(size) }
     * @return R
     */
    public static int size(final ArrayQueueADT queue) {
        return queue.size;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#isEmpty(ArrayQueueADT)}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code R == (size == 0) && immutable(size) && size' == size }
     * @return R
     */
    public static boolean isEmpty(final ArrayQueueADT queue) {
        return queue.size == 0;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#clear(ArrayQueueADT)}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code size' == 0 }
     */
    public static void clear(final ArrayQueueADT queue) {
        queue.size = 0;
        queue.head = 0;
        queue.elements = new Object[2];
    }
}
