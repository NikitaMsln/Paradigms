package expression.exceptions;

public class DivisionByZeroException extends ExpressionException {
    public DivisionByZeroException(String message) {
        super("Division by zero", message);
    }

    public DivisionByZeroException(int x, int y) {
        this(String.format("%d / %d", x, y));
    }
}
