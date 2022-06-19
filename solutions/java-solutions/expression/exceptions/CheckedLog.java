package expression.exceptions;

import expression.AbstractBinaryOperation;
import expression.CommonExp;
import expression.exceptions.IllegalLogArgumentException;

public class CheckedLog extends AbstractBinaryOperation {
    public CheckedLog(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        if (y <= 1) {
            throw new IllegalLogArgumentException("illegal base", x);
        }
        if (x <= 0) {
            throw new IllegalLogArgumentException("illegal argument", y);
        }
        for (int i = 0; true; i++) {
            x = x / y;
            if (x == 0) {
                return i;
            }
        }
    }

    @Override
    public String getOperator() {
        return "//";
    }

    @Override
    public int getPriority() {
        return 752;
    }
}
