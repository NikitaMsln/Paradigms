package expression.generic.operations;

public class Multiply<T> extends AbstractBinaryOperation<T> {
    public Multiply(CommonExp<T> expression1, CommonExp<T> expression2) {
        super(expression1, expression2);
    }

    @Override
    public T makeOp(T x, T y) {
        return executor.multiply(x, y);
    }

    @Override
    public String getOperator() {
        return "*";
    }

    @Override
    public int getPriority() {
        return 507;
    }
}
