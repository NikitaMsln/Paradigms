package queue;

import java.util.Objects;

/**
 * This: abstract data type mode
 * <h3> Class: {@link queue.ArrayQueueADT}</h3>
 * <i>Model</i>: {@code a[0 .. size - 1], first in (in tail) -- first out (from head)}
 * <br/>
 * <i>Invariant</i>: {@code size >= 0 && for i in [0, size): a[i] != null && a[0] -- queue head && a[size - 1] -- queue tail}
 * (tail -> a[size - 1], head -> a[0])
 * <br/>
 * Let <b>saveOrder</b>{@code (array1, head1, array2, head2, cnt): for i in [0, cnt) array1[head1 + i] == array2[head2 + i]}
 * <br/>
 * Let <b>immutable</b>{@code (n) = saveOrder(a, 0, a', 0, n)}
 * @see queue.ArrayQueueModule
 * @see queue.ArrayQueue
 */
public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int head = 0, size = 0;

    /**
     * <h3>Function {@link queue.ArrayQueueADT#enqueue(ArrayQueueADT, Object)}</h3>
     * Pred: {@code element != null && queue != null}
     * <br>
     * Post: {@code size' == size + 1 && a[size'] == element && immutable(size)}
     * @param queue queue of Objects
     * @param element not null object
     * @throws NullPointerException if one of argument is null
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
     * @param queue queue of Objects
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
     * Pred: {@code size >= 1 && queue != null}
     * <br>
     * Post: {@code R == a[0] && immutable(size) && size' == size}
     * @param queue queue of Objects
     * @return R -- not null object
     * @throws AssertionError if size == 0
     * @throws NullPointerException if queue is null
     */
    public static Object element(final ArrayQueueADT queue) {
        assert queue.size >= 1;
        return queue.elements[queue.head];
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#dequeue(ArrayQueueADT)}</h3>
     * Pred: {@code size >= 1 && queue != null}
     * <br>
     * Post: {@code R == a[0] && size' == size - 1 && saveOrder(a, 1, a', 0, size')}
     * @param queue queue of Objects
     * @return R -- not null object
     * @throws AssertionError if size == 0
     * @throws NullPointerException if queue is null
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
     * Pred: {@code queue != null}
     * <br>
     * Post: {@code R == size && size' == size && immutable(size) }
     * @param queue queue of Objects
     * @return R
     * @throws NullPointerException if queue is null
     */
    public static int size(final ArrayQueueADT queue) {
        return queue.size;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#isEmpty(ArrayQueueADT)}</h3>
     * Pred: {@code queue != null}
     * <br>
     * Post: {@code R == (size == 0) && immutable(size) && size' == size }
     * @param queue queue of Objects
     * @return R
     * @throws NullPointerException if queue is null
     */
    public static boolean isEmpty(final ArrayQueueADT queue) {
        return queue.size == 0;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#clear(ArrayQueueADT)}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code size' == 0 }
     * @param queue queue of Objects
     */
    public static void clear(final ArrayQueueADT queue) {
        queue.size = 0;
        queue.head = 0;
        queue.elements = new Object[2];
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#push(ArrayQueueADT, Object)}</h3>
     * Pred: {@code element != null}
     * <br>
     * Post: {@code size' == size + 1 && a'[0] == element && saveOrder(a, 0, a', 1, size) }
     * @param queue queue of Objects
     * @param element not null Object
     * @throws NullPointerException if element is null
     */
    public static void push(final ArrayQueueADT queue, final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, queue.size + 1);
        queue.head = (queue.head - 1 + queue.elements.length) % queue.elements.length;
        queue.elements[queue.head] = element;
        queue.size++;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#peek(ArrayQueueADT)}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[size - 1] && immutable(size) && size' == size }
     * @param queue queue of Objects
     * @return R -- not null Object
     * @throws AssertionError if size == 0
     */
    public static Object peek(final ArrayQueueADT queue) {
        assert queue.size >= 1;
        int tail = (queue.head + queue.size - 1 + queue.elements.length) % queue.elements.length;
        return queue.elements[tail];
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#remove(ArrayQueueADT)}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[size - 1] && size' == size - 1 && immutable(size') }
     * @param queue queue of Objects
     * @return R -- not null Object
     * @throws AssertionError if size == 0
     */
    public static Object remove(final ArrayQueueADT queue) {
        assert queue.size >= 1;
        int ind = (queue.head + queue.size - 1) % queue.elements.length;
        Object element = queue.elements[ind];
        queue.elements[ind] = null;
        queue.size--;
        if (queue.head == queue.elements.length) {
            queue.head = 0;
        }
        return element;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#indexOf(ArrayQueueADT, Object)}</h3>
     * Pred: {@code expect != null }
     * <br>
     * Post: {@code R == χ(a, expect) * [1 + min { d in [0, size) : a[d] == expect }] - 1 && size' == size && immutable(size) }
     * @param queue queue of Objects
     * @param expect not null Object
     * @return R -- integer in [-1, size)
     * @throws NullPointerException if expect is null
     */
    public static int indexOf(final ArrayQueueADT queue, final Object expect) {
        Objects.requireNonNull(expect);
        int cnt = -1;
        int length = queue.elements.length;
        while (++cnt < queue.size) {
            if (queue.elements[(queue.head + cnt) % length].equals(expect)) {
                return cnt;
            }
        }
        return -1;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueADT#lastIndexOf(ArrayQueueADT, Object)}</h3>
     * Pred: {@code expect != null }
     * <br>
     * Post: {@code R == χ(a, expect) * [1 + max { d in [0, size) : a[d] == expect }] - 1 && size' == size && immutable(size) }
     * @param queue queue of Objects
     * @param expect not null Object
     * @return R -- integer in {@code [-1, size)}
     * @throws NullPointerException if expect is null
     */
    public static int lastIndexOf(final ArrayQueueADT queue, final Object expect) {
        Objects.requireNonNull(expect);
        int cnt = queue.size;
        int length = queue.elements.length;
        while (--cnt >= 0) {
            if (queue.elements[(queue.head + cnt) % length].equals(expect)) {
                return cnt;
            }
        }
        return -1;
    }
}
