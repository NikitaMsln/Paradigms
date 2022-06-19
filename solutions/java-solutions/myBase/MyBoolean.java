package myBase;

import java.util.Objects;

/**
 * <h3>Class {@link MyBoolean}</h3>
 * The MyBoolean class wraps a value of the primitive type boolean in an object.
 *      Can update value of boolean and return new value.
 * @see Update
 * @see queue.AbstractQueue
 * @see queue.Queue
 */
public class MyBoolean {
    /**
     * Value of the boolean
     */
    private boolean bool;

    public MyBoolean(boolean bool) {
        this.bool = bool;
    }

    /**
     * <h3>Function {@link MyBoolean#update(Update, boolean)}</h3>
     * Pred: {@code update != null}
     * <br/>
     * Post: {@code bool' == bool update.updateAndGet second && R == bool'}
     * @param update non-null Update
     * @param second boolean
     * @return R -- new value of boolean
     * @throws NullPointerException if update is null
     */
    public boolean update(Update update, boolean second) {
        bool = Objects.requireNonNull(update).updateAndGet(bool, second);
        return bool;
    }

    @Override
    public String toString() {
        return Boolean.toString(bool);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Boolean || obj instanceof MyBoolean) {
            return Boolean.valueOf(bool).equals(obj);
        }
        return false;
    }
}
