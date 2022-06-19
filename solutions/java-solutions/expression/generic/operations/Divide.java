package expression.generic.operations;

public class Divide<T> extends AbstractBinaryOperation<T> {
    public Divide(CommonExp<T> expression1, CommonExp<T> expression2) {
        super(expression1, expression2);
    }

    @Override
    public T makeOp(T x, T y) {
        return executor.divide(x, y);
    }

    @Override
    public String getOperator() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 552;
    }
}
