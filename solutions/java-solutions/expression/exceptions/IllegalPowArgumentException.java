package expression.exceptions;

public class IllegalPowArgumentException extends IllegalFunctionArgumentException {
    public IllegalPowArgumentException(String message, int a) {
        super(message, "myBase.pow", a);
    }
}
