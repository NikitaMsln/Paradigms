package expression.generic.operations;

import java.util.Objects;

public abstract class AbstractUnaryOperation<T> extends AbstractExpression<T> implements UnOperation<T> {
    private final CommonExp<T> expression;

    protected AbstractUnaryOperation(CommonExp<T> expression) {
        super(expression.getExecutor());
        this.expression = expression;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return makeOp(expression.evaluate(x, y, z));
    }

    @Override
    public int getPriority() {
        return 900;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractUnaryOperation exp) {
            return exp.getOperator().equals(getOperator()) && exp.expression.equals(expression);
        }
        return false;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(getOperator()).append("(");
        expression.toString(sb);
        sb.append(")");
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        sb.append(getOperator());
        if (expression.getPriority() >= this.getPriority()) {
            sb.append(" ");
            expression.toMiniString(sb);
        } else {
            sb.append("(");
            expression.toMiniString(sb);
            sb.append(")");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, getOperator());
    }
}
