package expression.generic.operations;

public class Subtract<T> extends AbstractBinaryOperation<T> {
    public Subtract(CommonExp<T> expression1, CommonExp<T> expression2) {
        super(expression1, expression2);
    }

    @Override
    public T makeOp(T x, T y) {
        return executor.subtract(x, y);
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 352;
    }
}
