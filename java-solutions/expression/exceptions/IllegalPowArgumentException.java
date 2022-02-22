package expression.exceptions;

public class IllegalPowArgumentException extends IllegalFunctionArgumentException {
    public IllegalPowArgumentException(String message, int a) {
        super(message, "pow", a);
    }
}
