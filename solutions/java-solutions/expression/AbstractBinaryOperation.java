package expression;

import expression.exceptions.OverflowException;

public abstract class AbstractBinaryOperation extends AbstractExpression implements BinOperation {
    private final CommonExp expression1;
    private final CommonExp expression2;

    protected AbstractBinaryOperation(CommonExp expression1, CommonExp expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public int evaluate(int x) {
        return makeOp(expression1.evaluate(x), expression2.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
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

    private void toMiniString(CommonExp expression, boolean brackets, StringBuilder sb) {
        sb.append(brackets ? "(" : "");
        expression.toMiniString(sb);
        sb.append(brackets ? ")" : "");
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(expression1, expression2, getOperator());
    }

    protected OverflowException overflow(int x, int y) {
        return new OverflowException(String.format("%d %s %d", x, getOperator(), y));
    }
}
