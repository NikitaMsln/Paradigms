package expression.generic.operations;

import expression.generic.executors.Executor;

public class Const<T> extends AbstractVarConst<T> {
    private final T value;

    public Const(Executor<T> executor, String x) {
        super(executor, x);
        value = executor.parseConst(x);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }
}