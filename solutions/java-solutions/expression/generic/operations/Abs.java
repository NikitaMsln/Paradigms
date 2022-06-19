package expression.generic.operations;

public class Abs<T> extends AbstractUnaryOperation<T> {
    public Abs(CommonExp<T> expression) {
        super(expression);
    }

    @Override
    public String getOperator() {
        return "abs";
    }

    @Override
    public T makeOp(T x) {
        return executor.abs(x);
    }
}
