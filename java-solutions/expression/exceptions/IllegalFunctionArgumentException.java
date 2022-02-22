package expression.exceptions;

public class IllegalFunctionArgumentException extends ExpressionException {
    public IllegalFunctionArgumentException(String message, String f, int a) {
        super(String.format("%s: argument %d is out of domain of a function %s", message, a, f));
    }
}
