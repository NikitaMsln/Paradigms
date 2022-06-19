package expression.generic.operations;

public class Negate<T> extends AbstractUnaryOperation<T> {
    public Negate(CommonExp<T> expression) {
        super(expression);
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public T makeOp(T x) {
        return executor.negate(x);
    }
}
