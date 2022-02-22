package queue;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Queues {
    /* package-private */ interface QueueModel {
        @ReflectionTest.Ignore
        ArrayDeque<Object> model();

        default Object dequeue() {
            return model().removeFirst();
        }

        default int size() {
            return model().size();
        }

        default boolean isEmpty() {
            return model().isEmpty();
        }

        default void clear() {
            model().clear();
        }

        default void enqueue(final Object element) {
            model().addLast(element);
        }

        default Object element() {
            return model().getFirst();
        }
    }

    /* package-private */ interface QueueChecker<T extends QueueModel> {
        T wrap(ArrayDeque<Object> reference);

        default List<T> linearTest(final T queue, final Random random) {
            // Do nothing by default
            return List.of();
        }

        default void check(final T queue, final Random random) {
            queue.element();
        }

        default void add(final T queue, final Object element, final Random random) {
            queue.enqueue(element);
        }

        default Object randomElement(final Random random) {
            return ArrayQueueTester.ELEMENTS[random.nextInt(ArrayQueueTester.ELEMENTS.length)];
        }

        default void remove(final T queue, final Random random) {
            queue.dequeue();
        }

        @SuppressWarnings("unchecked")
        default T cast(final QueueModel model) {
            return (T) model;
        }
    }

    @FunctionalInterface
    protected interface Splitter<M extends QueueModel> {
        List<M> split(final QueueChecker<? extends M> tester, final M queue, final Random random);
    }

    @FunctionalInterface
    protected interface LinearTester<M extends QueueModel> extends Splitter<M> {
        void test(final QueueChecker<? extends M> tester, final M queue, final Random random);

        @Override
        default List<M> split(final QueueChecker<? extends M> tester, final M queue, final Random random) {
            test(tester, queue, random);
            return List.of();
        }
    }
}
