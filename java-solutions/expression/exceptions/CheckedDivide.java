package expression.exceptions;

import expression.CommonExp;
import expression.Divide;
import expression.exceptions.DivisionByZeroException;

public class CheckedDivide extends Divide {
    public CheckedDivide(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        if (y == 0) {
            throw new DivisionByZeroException(x, y);
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw overflow(x, y);
        }
        return super.makeOp(x, y);
    }
}
