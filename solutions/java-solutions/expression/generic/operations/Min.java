package expression.generic.operations;

public class Min<T> extends AbstractBinaryOperation<T> {
    public Min(CommonExp<T> expression1, CommonExp<T> expression2) {
        super(expression1, expression2);
    }

    @Override
    public T makeOp(T x, T y) {
        return executor.min(x, y);
    }

    @Override
    public String getOperator() {
        return "min";
    }

    @Override
    public int getPriority() {
        return 108;
    }
}
