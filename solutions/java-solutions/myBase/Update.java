package myBase;

/**
 * <h3>Class (i) {@link Update}</h3>
 * Functional interface with function {@link Update#updateAndGet(boolean, boolean)}
 * @see MyBoolean
 * @see queue.AbstractQueue
 */
@FunctionalInterface
public interface Update {
    /**
     * <h3>Function {@link Update#updateAndGet(boolean, boolean)}</h3>
     * <p>
     *     This function make binary operation: {@code R == first updateAndGet second}
     * </p>
     * @param first boolean
     * @param second boolean
     * @return R
     */
    boolean updateAndGet(boolean first, boolean second);
}