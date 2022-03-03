package queue;

import java.util.Objects;

/**
 * This: module mode
 * <h3> Class: {@link queue.ArrayQueueModule}</h3>
 * <i>Model</i>: {@code a[0 .. size - 1], first in (in tail) -- first out (from head)}
 * <br/>
 * <i>Invariant</i>: {@code size >= 0 && for i in [0, size): a[i] != null && a[0] -- queue head && a[size - 1] -- queue tail}
 * (tail -> a[size - 1], head -> a[0])
 * <br/>
 * Let <b>saveOrder</b>{@code (array1, head1, array2, head2, cnt): for i in [0, cnt) array1[head1 + i] == array2[head2 + i]}
 * <br/>
 * Let <b>immutable</b>{@code (n) = saveOrder(a, 0, a', 0, n)}
 * @see queue.ArrayQueueADT
 * @see queue.ArrayQueue
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

    /**
     * <h3>Function {@link queue.ArrayQueueModule#push(Object)}</h3>
     * Pred: {@code element != null}
     * <br>
     * Post: {@code size' == size + 1 && a'[0] == element && saveOrder(a, 0, a', 1, size) }
     * @param element not null Object
     * @throws NullPointerException if element is null
     */
    public static void push(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#peek()}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[size - 1] && immutable(size) && size' == size }
     * @return R -- not null Object
     * @throws AssertionError if size == 0
     */
    public static Object peek() {
        assert size >= 1;
        return elements[(head + size - 1) % elements.length];
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#remove()}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[size - 1] && size' == size - 1 && immutable(size') }
     * @return R -- not null Object
     * @throws AssertionError if size == 0
     */
    public static Object remove() {
        assert size >= 1;
        int ind = (head + size - 1) % elements.length;
        Object element = elements[ind];
        elements[ind] = null;
        size--;
        return element;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#indexOf(Object)}</h3>
     * Pred: {@code expect != null }
     * <br>
     * Post: {@code R == χ(a, expect) * [1 + min { d in [0, size) : a[d] == expect }] - 1 && size' == size && immutable(size) }
     * @param expect not null Object
     * @return R -- integer in {@code [-1, size)}
     * @throws NullPointerException if expect is null
     */
    public static int indexOf(final Object expect) {
        Objects.requireNonNull(expect);
        int cnt = -1;
        int length = elements.length;
        while (++cnt < size) {
            if (elements[(head + cnt) % length].equals(expect)) {
                return cnt;
            }
        }
        return -1;
    }

    /**
     * <h3>Function {@link queue.ArrayQueueModule#lastIndexOf(Object)}</h3>
     * Pred: {@code expect != null }
     * <br>
     * Post: {@code R == χ(a, expect) * [1 + max { d in [0, size) : a[d] == expect }] - 1 && size' == size && immutable(size) }
     * @param expect not null Object
     * @return R -- integer in {@code [-1, size)}
     * @throws NullPointerException if expect is null
     */
    public static int lastIndexOf(final Object expect) {
        Objects.requireNonNull(expect);
        int cnt = size;
        int length = elements.length;
        while (--cnt >= 0) {
            if (elements[(head + cnt) % length].equals(expect)) {
                return cnt;
            }
        }
        return -1;
    }
}
