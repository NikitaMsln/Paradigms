package expression.exceptions;

import expression.CommonExp;
import expression.Negate;

public class CheckedNegate extends Negate {
    public CheckedNegate(CommonExp expression) {
        super(expression);
    }

    @Override
    public int makeOp(int x) {
        if (x == Integer.MIN_VALUE) {
            throw overflow(x);
        }
        return super.makeOp(x);
    }
}
