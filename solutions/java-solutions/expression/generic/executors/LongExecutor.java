package expression.generic.executors;

public class LongExecutor extends AbstractExecutor<Long> {
    @Override
    public Long add(Long x, Long y) {
        return x + y;
    }

    @Override
    public Long multiply(Long x, Long y) {
        return x * y;
    }

    @Override
    public Long divide(Long x, Long y) {
        if (y.equals(ZERO)) {
            throw division(x, y);
        }
        return x / y;
    }

    @Override
    public Long min(Long x, Long y) {
        return Long.min(x, y);
    }

    @Override
    public Long max(Long x, Long y) {
        return Long.max(x, y);
    }

    @Override
    public Long count(Long x) {
        return (long) Long.bitCount(x);
    }

    @Override
    public Long parseConst(String x) {
        return Long.parseLong(x);
    }

    @Override
    public Long parseConst(int x) {
        return (long) x;
    }

    @Override
    public int getTypePriority() {
        return 200;
    }
}
