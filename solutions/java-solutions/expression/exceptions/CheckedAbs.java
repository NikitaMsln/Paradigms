package expression.exceptions;

import expression.Abs;
import expression.CommonExp;

public class CheckedAbs extends Abs {
    public CheckedAbs(CommonExp expression) {
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
