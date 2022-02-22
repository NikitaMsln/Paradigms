package expression;

public class Subtract extends AbstractBinaryOperation {
    public Subtract(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        return x - y;
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
