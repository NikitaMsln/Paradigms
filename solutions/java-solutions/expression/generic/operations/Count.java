package expression.generic.operations;

public class Count<T> extends AbstractUnaryOperation<T> {
    public Count(CommonExp<T> expression) {
        super(expression);
    }

    @Override
    public String getOperator() {
        return "count";
    }

    @Override
    public T makeOp(T x) {
        return executor.count(x);
    }
}
