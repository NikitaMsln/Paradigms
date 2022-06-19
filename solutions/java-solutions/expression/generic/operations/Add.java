package expression.generic.operations;

public class Add<T> extends AbstractBinaryOperation<T> {
    public Add(CommonExp<T> expression1, CommonExp<T> expression2) {
        super(expression1, expression2);
    }

    @Override
    public T makeOp(T x, T y) {
        return executor.add(x, y);
    }

    @Override
    public String getOperator() {
        return "+";
    }

    @Override
    public int getPriority() {
        return 302;
    }
}
