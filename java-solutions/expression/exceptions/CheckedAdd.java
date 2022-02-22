package expression.exceptions;

import expression.Add;
import expression.CommonExp;

public class CheckedAdd extends Add {
    public CheckedAdd(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        if (y > 0 && Integer.MAX_VALUE - y < x || y < 0 && Integer.MIN_VALUE - y > x) {
            throw overflow(x, y);
        }
        return super.makeOp(x, y);
    }
}
