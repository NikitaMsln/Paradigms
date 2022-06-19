package expression;

public class Divide extends AbstractBinaryOperation {
    public Divide(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        return x / y;
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
