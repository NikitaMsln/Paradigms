package expression.generic.executors;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public abstract class AbstractExecutor<T extends Number & Comparable<T>> implements Executor<T> {
    protected final T ZERO = parseConst(0);
    private final T MINUS_ONE = parseConst(-1);

    @Override
    public T subtract(T x, T y) {
        return add(x, negate(y));
    }

    @Override
    public T abs(T x) {
        if (x.compareTo(ZERO) < 0) {
            return negate(x);
        }
        return x;
    }

    @Override
    public T negate(T x) {
        return multiply(x, MINUS_ONE);
    }

    @Override
    public Executor<T> max(Executor<T> x) {
        return getTypePriority() < x.getTypePriority() ? x : this;
    }

    protected OverflowException overflow(int x, String operator, int y) {
        return new OverflowException(String.format("%d %s %d", x, operator, y));
    }

    protected OverflowException overflow(String operator, int x) {
        return new OverflowException(String.format("%s %d", operator, x));
    }

    protected OverflowException overflow(String x) {
        return new OverflowException(String.format("const(%s)", x));
    }

    protected DivisionByZeroException division(T x, T y) {
        return new DivisionByZeroException(String.format("%s / %s", x, y));
    }
}
