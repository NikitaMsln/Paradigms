package expression;

public class T_Zeroes extends AbstractUnaryOperation {
    public T_Zeroes(CommonExp expression) {
        super(expression);
    }

    @Override
    public String getOperator() {
        return "t0";
    }

    @Override
    public int makeOp(int x) {
        return Integer.numberOfTrailingZeros(x);
    }
}
