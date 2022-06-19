package queue;

import java.util.function.Predicate;

/**
 * This: interface for {@link queue.AbstractQueue} <br/>
 * <h3> Class (i): {@link queue.Queue} </h3>
 * <i>Model</i>: {@code a[0 .. size - 1], first in (in tail) -- first out (from head)}
 * <br/>
 * <i>Invariant</i>: {@code size >= 0 && for i in [0, size): a[i] != null && a[0] -- queue head && a[size - 1] -- queue tail}
 * (tail -> a[size - 1], head -> a[0])
 * <br/>
 * Let <b>saveOrder</b>{@code (array1: Object[], head1: int, array2: Object[], head2: int, cnt: int):
 *          for i in [0, cnt) array1[head1 + i] == array2[head2 + i]}
 * <br/>
 * Let <b>immutable</b>{@code (n: int) = saveOrder(a, 0, a', 0, n)}
 * <br/>
 * Let <b>orderLike</b>{@code (array1: Object[], array2: Object[]) = array1 in array2
 *      && i < j where i,j in array1.indices => array2^(-1)[array1[i]] < array2^(-1)[array1[j]]}
 * @see queue.ArrayQueueModule
 * @see queue.ArrayQueueADT
 * @see queue.ArrayQueue
 */
public interface Queue {
    /**
     * <h3>Function {@link Queue#enqueue(Object)}</h3>
     * Pred: {@code element != null}
     * <br>
     * Post: {@code size' == size + 1 && a[size'] == element && immutable(size)}
     * @param element not null object
     * @throws NullPointerException if element is null
     */
    void enqueue(Object element);

    /**
     * <h3>Function {@link Queue#dequeue()}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[0] && size' == size - 1 && saveOrder(a, 1, a', 0, size')}
     * @return R -- not null object
     * @throws AssertionError if size == 0
     */
    Object dequeue();

    /**
     * <h3>Function {@link Queue#element()}</h3>
     * Pred: {@code size >= 1}
     * <br>
     * Post: {@code R == a[0] && immutable(size) && size' == size}
     * @return R -- not null object
     * @throws AssertionError if size == 0
     */
    Object element();

    /**
     * <h3>Function {@link Queue#size()}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code R == size && size' == size && immutable(size) }
     * @return R
     */
    int size();

    /**
     * <h3>Function {@link Queue#isEmpty()}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code R == (size == 0) && immutable(size) && size' == size }
     * @return R
     */
    boolean isEmpty();

    /**
     * <h3>Function {@link Queue#clear()}</h3>
     * Pred: {@code true}
     * <br>
     * Post: {@code size' == 0 }
     */
    void clear();

    /**
     * <h3>Function {@link Queue#removeIf(Predicate)}</h3>
     * Pred: {@code predicate != null}
     * <br>
     * Post: {@code orderLike(a', a)
     *      && OR[predicate.test(i) for i in a'] == false
     *      && AND[predicate.test(i) for i in a\a'] == true}
     * @param predicate non-null predicate
     * @throws NullPointerException if predicate is null
     */
    void removeIf(Predicate<Object> predicate);

    /**
     * <h3>Function {@link Queue#retainIf(Predicate)}</h3>
     * Pred: {@code predicate != null}
     * <br>
     * Post: {@code orderLike(a', a)
     *      && AND[predicate.test(i) for i in a'] == true
     *      && OR[predicate.test(i) for i in a\a'] == false}
     * @param predicate non-null predicate
     * @throws NullPointerException if predicate is null
     */
    void retainIf(Predicate<Object> predicate);

    /**
     * <h3>Function {@link Queue#takeWhile(Predicate)}</h3>
     * Pred: {@code predicate != null}
     * <br>
     * Post: {@code && size' == max { d in [0, size) | AND[predicate.test(a[i]) for i in [0, d)] == true }
     *      && immutable(size')}
     * @param predicate non-null predicate
     * @throws NullPointerException if predicate is null
     */
    void takeWhile(Predicate<Object> predicate);

    /**
     * <h3>Function {@link Queue#dropWhile(Predicate)}</h3>
     * Pred: {@code predicate != null}
     * <br>
     * let {@code q == max { d in [0, size) | OR[predicate.test(a[i]) for i in [0, d)] == false }} <br/>
     * Post: {@code size' == size - q && saveOrder(a', 0, a, q, size')}
     * @param predicate non-null predicate
     * @throws NullPointerException if predicate is null
     */
    void dropWhile(Predicate<Object> predicate);
}
