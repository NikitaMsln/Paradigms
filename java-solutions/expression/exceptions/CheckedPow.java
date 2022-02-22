package expression.exceptions;

import expression.AbstractBinaryOperation;
import expression.CommonExp;

public class CheckedPow extends AbstractBinaryOperation {
    public CheckedPow(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        if (y < 0) {
            if (x == 0) {
                throw new DivisionByZeroException(String.format("%d ** %d", x, y));
            }
            throw new IllegalPowArgumentException("illegal degree", y);
        }
        if (x == 0 && y == 0) {
            throw new IllegalPowArgumentException("0^0 can't be evaluate", y);
        }
        int ans = 1, x0 = x, y0 = y;
        while (true) {
            if (y % 2 == 1) {
                if (checkOverflowPow(x, ans)) {
                    throw overflow(x0, y0);
                }
                ans *= x;
            }
            y /= 2;
            if (y == 0) {
                break;
            }
            if (checkOverflowPow(x, x)) {
                throw overflow(x0, y0);
            }
            x *= x;
        }
        return ans;
    }

    private boolean checkOverflowPow(int x, int y) {
        return CheckedMultiply.checkOverflowMultiply(x, y);
    }

    @Override
    public String getOperator() {
        return "**";
    }

    @Override
    public int getPriority() {
        return 752;
    }
}
