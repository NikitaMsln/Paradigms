package queue;

import java.util.Objects;

/**
 * This: class mode
 * <br/>
 * <h3> Class: {@link queue.ArrayQueue} extends {@link queue.AbstractQueue} implements {@link queue.Queue} </h3>
 */
public class ArrayQueue extends AbstractQueue {
    private Object[] elements;
    private int head;

    @Override
    protected void init() {
        head = 0;
        elements = new Object[2];
    }

    @Override
    protected void pushBack(final Object element, int size) {
        ensureCapacity(size + 1);
        int tail = (head + size) % elements.length;
        elements[tail] = element;
    }

    /**
     * <h3>Function {@link ArrayQueue#ensureCapacity(int)}</h3>
     * @param capacity integer
     */
    private void ensureCapacity(final int capacity) {
        if (elements.length < capacity) {
            Object[] newElements = new Object[capacity * 2];
            System.arraycopy(elements, head, newElements, 0, elements.length - head);
            System.arraycopy(elements, 0, newElements, elements.length - head, head);
            elements = newElements;
            head = 0;
        }
    }

    @Override
    protected void popFront() {
        elements[head] = null;
        if (++head == elements.length) {
            head = 0;
        }
    }

    @Override
    protected Object elementImpl() {
        return elements[head];
    }

    ////////////////////////////////////////////////////////////////////////
    //                       Modification DequeIndex                     //

    /**
     * <h3>Function {@link queue.ArrayQueue#push(Object)}</h3>
     * Pred: {@code element != null}
     * <br>
     * Post: {@code size' == size + 1 && a'[0] == element && saveOrder(a, 0, a', 1, size) }
     * @param element not null Object
     * @throws NullPointerException if element is null
     */
    public void push(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size() + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    /**
     * <h3>Function {@link queue.ArrayQueue#peek()}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[size - 1] && immutable(size) && size' == size }
     * @return R -- not null Object
     * @throws AssertionError if size == 0
     */
    public Object peek() {
        assert size() >= 1;
        int tail = (head + size() - 1 + elements.length) % elements.length;
        return elements[tail];
    }

    /**
     * <h3>Function {@link queue.ArrayQueue#remove()}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[size - 1] && size' == size - 1 && immutable(size') }
     * @return R -- not null Object
     * @throws AssertionError if size == 0
     */
    public Object remove() {
        assert size() >= 1;
        int ind = (head + size() - 1) % elements.length;
        Object element = elements[ind];
        elements[ind] = null;
        size--;
        if (head == elements.length) {
            head = 0;
        }
        return element;
    }

    /**
     * <h3>Function {@link queue.ArrayQueue#indexOf(Object)}</h3>
     * Pred: {@code expect != null }
     * <br>
     * Post: {@code R == χ(a, expect) * [1 + min { d in [0, size) : a[d] == expect }] - 1 && size' == size && immutable(size) }
     * @param expect not null Object
     * @return R -- integer in {@code [-1, size)}
     * @throws NullPointerException if expect is null
     */
    public int indexOf(final Object expect) {
        Objects.requireNonNull(expect);
        int cnt = -1;
        int length = elements.length;
        while (++cnt < size()) {
            if (elements[(head + cnt) % length].equals(expect)) {
                return cnt;
            }
        }
        return -1;
    }

    /**
     * <h3>Function {@link queue.ArrayQueue#lastIndexOf(Object)}</h3>
     * Pred: {@code expect != null }
     * <br>
     * Post: {@code R == χ(a, expect) * [1 + max { d in [0, size) : a[d] == expect }] - 1 && size' == size && immutable(size) }
     * @param expect not null Object
     * @return R -- integer in {@code [-1, size)}
     * @throws NullPointerException if expect is null
     */
    public int lastIndexOf(final Object expect) {
        Objects.requireNonNull(expect);
        int cnt = size();
        int length = elements.length;
        while (--cnt >= 0) {
            if (elements[(head + cnt) % length].equals(expect)) {
                return cnt;
            }
        }
        return -1;
    }
}
