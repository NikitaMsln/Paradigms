package expression.exceptions;

import expression.CommonExp;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        if (checkOverflowMultiply(x, y)) {
            throw overflow(x, y);
        }
        return super.makeOp(x, y);
    }

    public static boolean checkOverflowMultiply(int a, int b) {
        if (a > 0 && b > 0) {
            return a > Integer.MAX_VALUE / b;
        } else if (a < 0 && b < 0) {
            return b < Integer.MAX_VALUE / a;
        } else if (a < 0 && b > 0) {
            return a < Integer.MIN_VALUE / b;
        } else if (a > 0 && b < 0) {
            return b < Integer.MIN_VALUE / a;
        } else {
            return false;
        }
    }
}
