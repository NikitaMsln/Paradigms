package expression.generic.operations;

public class Max<T> extends AbstractBinaryOperation<T> {
    public Max(CommonExp<T> expression1, CommonExp<T> expression2) {
        super(expression1, expression2);
    }

    @Override
    public T makeOp(T x, T y) {
        return executor.max(x, y);
    }

    @Override
    public String getOperator() {
        return "max";
    }

    @Override
    public int getPriority() {
        return 108;
    }
}
