package queue;

import myBase.MyBoolean;
import myBase.Update;

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
        magicAction(false, (f, s) -> !s, predicate);
    }

    @Override
    public void retainIf(Predicate<Object> predicate) {
        magicAction(true, (f, s) -> s, predicate);
    }

    @Override
    public void takeWhile(Predicate<Object> predicate) {
        magicAction(true, (f, s) -> f & s, predicate);
    }

    @Override
    public void dropWhile(Predicate<Object> predicate) {
        magicAction(false, (f, s) -> f | !s, predicate);
    }

    /**
     * <h3>Function {@link AbstractQueue#magicAction(boolean, Update, Predicate)}</h3>
     * Pred: {@code update != null && predicate != null}
     * <br/>
     * Let <b>.upd</b>{@code (x: Object) = .update(update, predicate.test(x))}
     * <br/>
     * Let <b>updateRange</b>{@code (a: Object[], end: int) =
     *          start.upd(a[0]).upd(a[1]). ... .upd(a[end])}
     * <br/>
     * Post: {@code orderLike(a', a) && AND[updateRange(a, a^(-1)[i]) for i in a'] == true
     *      && OR[updateRange(a, a^(-1)[i]) for i in a\a'] == false}
     * @param start boolean to initialize {@link MyBoolean}
     * @param update for update value of {@link MyBoolean}
     * @param predicate non-null predicate
     * @throws NullPointerException if predicate or update is null
     */
    private void magicAction(boolean start, Update update, Predicate<Object> predicate) {
        assert predicate != null && update != null;
        MyBoolean bool = new MyBoolean(start);
        int sizeTmp = size();
        for (int i = 0; i < sizeTmp; i++) {
            final Object tmp = dequeue();
            if (bool.update(update, predicate.test(tmp))) {
                enqueue(tmp);
            }
        }
    }
}
