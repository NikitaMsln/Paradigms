package expression.generic.operations;

public abstract class AbstractBinaryOperation<T> extends AbstractExpression<T> implements BinOperation<T> {
    private final CommonExp<T> expression1;
    private final CommonExp<T> expression2;

    protected AbstractBinaryOperation(CommonExp<T> expression1, CommonExp<T> expression2) {
        super(expression1.getExecutor().max(expression2.getExecutor()));
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return makeOp(expression1.evaluate(x, y, z), expression2.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractBinaryOperation exp) {
            return exp.getOperator().equals(getOperator())
                    && exp.expression1.equals(expression1)
                    && exp.expression2.equals(expression2);
        }
        return false;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append("(");
        expression1.toString(sb);
        sb.append(" ").append(getOperator()).append(" ");
        expression2.toString(sb);
        sb.append(")");
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        toMiniString(expression1, expression1.getPriority() / 100 < getPriority() / 100, sb);
        sb.append(" ").append(getOperator()).append(" ");
        toMiniString(expression2, rightPriority(expression2.getPriority()) < getPriority(), sb);
    }

    private int rightPriority(int n) {
        if (n % 100 >= 50) return n - 50;
        return n;
    }

    private void toMiniString(CommonExp<T> expression, boolean brackets, StringBuilder sb) {
        sb.append(brackets ? "(" : "");
        expression.toMiniString(sb);
        sb.append(brackets ? ")" : "");
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(expression1, expression2, getOperator());
    }
}
