package expression.exceptions;

public class OverflowException extends ExpressionException {
    public OverflowException(String operation) {
        super("Overflow", operation);
    }
}
