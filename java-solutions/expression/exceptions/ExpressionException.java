package expression.exceptions;

public class ExpressionException extends RuntimeException {
    public ExpressionException(String message, String operation) {
        this(String.format("%s while evaluating operation: %s", message, operation));
    }

    public ExpressionException(String message) {
        super(message);
    }
}
