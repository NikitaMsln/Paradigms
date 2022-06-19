package expression;

public class Abs extends AbstractUnaryOperation {
    public Abs(CommonExp expression) {
        super(expression);
    }

    @Override
    public String getOperator() {
        return "abs";
    }

    @Override
    public int makeOp(int x) {
        if (x > 0) return x;
        else return -x;
    }
}
