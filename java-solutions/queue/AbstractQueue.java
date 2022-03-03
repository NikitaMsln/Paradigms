package queue;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * This: abstract queue <br/>
 * <h3> Class (a): {@link queue.AbstractQueue} implements {@link queue.Queue} </h3>
 * @see queue.ArrayQueueModule
 * @see queue.ArrayQueueADT
 * @see queue.ArrayQueue
 */
public abstract class AbstractQueue implements Queue {
    protected int size;

    protected AbstractQueue() {
        init();
    }

    @Override
    public void enqueue(final Object element) {
        Objects.requireNonNull(element);
        pushBack(element, size);
        size++;
    }

    @Override
    public Object dequeue() {
        assert size >= 1;
        final Object result = element();
        size--;
        popFront();
        return result;
    }

    @Override
    public Object element() {
        assert size >= 1;
        return elementImpl();
    }

    /**
     * <h3>Function {@link AbstractQueue#init()}</h3>
     * clear queue <br>
     * Pred: {@code true}
     * <br>
     * Post: {@code size' == 0}
     */
    protected abstract void init();

    /**
     * <h3>Function {@link AbstractQueue#pushBack(Object, int)}</h3>
     * push object at end of queue<br>
     * Pred: {@code element != null}
     * <br>
     * Post: {@code size' == size + 1 && a[size'] == element && immutable(size)}
     * @param object non-null object
     * @param size integer
     */
    protected abstract void pushBack(final Object object, final int size);

    /**
     * <h3>Function {@link AbstractQueue#popFront()}</h3>
     * pop (non-null) object from front of queue<br/>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code size' == size - 1 && saveOrder(a, 1, a', 0, size')}
     */
    protected abstract void popFront();

    /**
     * <h3>Function {@link AbstractQueue#elementImpl()}</h3>
     * Pred: {@code size >= 1}
     * <br/>
     * Post: {@code R == a[0] && immutable(size) && size' == size}
     * @return R -- element from front of queue
     */
    protected abstract Object elementImpl();

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        init();
    }

    ////////////////////////////////////////////////////////////////////////
    //                        Modification IfWhile                       //

    @Override
    public void removeIf(Predicate<Object> predicate) {
        retainIf(predicate.negate());
    }

    @Override
    public void retainIf(Predicate<Object> predicate) {
        assert predicate != null;
        int sizeTmp = size();
        for (int i = 0; i < sizeTmp; i++) {
            final Object tmp = dequeue();
            if (predicate.test(tmp)) {
                enqueue(tmp);
            }
        }
    }

    @Override
    public void takeWhile(Predicate<Object> predicate) {
        final boolean[] prefIsTrue = { true };
        retainIf(e -> prefIsTrue[0] &= predicate.test(e));
    }

    @Override
    public void dropWhile(Predicate<Object> predicate) {
        final boolean[] prefIsFalse = { true };
        removeIf(e -> prefIsFalse[0] &= predicate.test(e));
    }
}
