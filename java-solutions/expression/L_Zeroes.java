package expression;

public class L_Zeroes extends AbstractUnaryOperation {
    public L_Zeroes(CommonExp expression) {
        super(expression);
    }

    @Override
    public String getOperator() {
        return "l0";
    }

    @Override
    public int makeOp(int x) {
        return Integer.numberOfLeadingZeros(x);
    }
}
