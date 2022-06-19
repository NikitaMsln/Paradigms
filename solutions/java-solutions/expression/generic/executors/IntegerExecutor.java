package expression.generic.executors;

import myBase.StringComparator;

import static expression.exceptions.CheckedMultiply.checkOverflowMultiply;

public class IntegerExecutor extends AbstractExecutor<Integer> {
    private final boolean needCheckOverflow;

    public IntegerExecutor(boolean check) {
        needCheckOverflow = check;
    }

    protected int trunc(int x) {
        return x;
    }

    @Override
    public Integer add(Integer x, Integer y) {
        if (needCheckOverflow && (y > 0 && Integer.MAX_VALUE - y < x || y < 0 && Integer.MIN_VALUE - y > x)) {
            throw overflow(x, "+", y);
        }
        return trunc(x + y);
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        if (needCheckOverflow && (y < 0 && Integer.MAX_VALUE + y < x || y > 0 && Integer.MIN_VALUE + y > x)) {
            throw overflow(x, "-", y);
        }
        return trunc(x - y);
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        if (needCheckOverflow && checkOverflowMultiply(x, y)) {
            throw overflow(x, "*", y);
        }
        return trunc(x * y);
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if (y.equals(ZERO)) {
            throw division(x, y);
        }
        if (needCheckOverflow) {
            if (x == Integer.MIN_VALUE && y == -1) {
                throw overflow(x, "/", y);
            }
        }
        return trunc(x / y);
    }

    @Override
    public Integer min(Integer x, Integer y) {
        return trunc(Integer.min(x, y));
    }

    @Override
    public Integer max(Integer x, Integer y) {
        return trunc(Integer.max(x, y));
    }

    @Override
    public Integer abs(Integer x) {
        if (needCheckOverflow && x == Integer.MIN_VALUE) {
            throw overflow("abs", x);
        }
        return trunc(Integer.signum(x) * x);
    }

    @Override
    public Integer negate(Integer x) {
        if (needCheckOverflow && x == Integer.MIN_VALUE) {
            throw overflow("-", x);
        }
        return trunc(-x);
    }

    @Override
    public Integer count(Integer x) {
        return trunc(Integer.bitCount(x));
    }

    @Override
    public Integer parseConst(String x) {
        if (needCheckOverflow && !StringComparator.isIntegerValue(x)) {
            throw overflow(x);
        }
        return trunc(Integer.parseInt(x));
    }

    @Override
    public Integer parseConst(int x) {
        return trunc(x);
    }

    @Override
    public int getTypePriority() {
        return 100;
    }
}
