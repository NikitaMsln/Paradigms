package expression.generic.executors;

public class TruncIntegerExecutor extends IntegerExecutor {
    public TruncIntegerExecutor(boolean check) {
        super(check);
    }

    @Override
    protected int trunc(int x) {
        return x / 10 * 10;
    }
}
