package expression;

import expression.exceptions.OverflowException;

import java.util.Objects;

public abstract class AbstractUnaryOperation extends AbstractExpression implements UnOperation {
    private final CommonExp expression;

    protected AbstractUnaryOperation(CommonExp expression) {
        this.expression = expression;
    }

    @Override
    public int evaluate(int x) {
        return makeOp(expression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
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

    protected OverflowException overflow(int x) {
        return new OverflowException(String.format("%s %d", getOperator(), x));
    }
}
