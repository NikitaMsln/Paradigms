package expression.exceptions;

public class IllegalLogArgumentException extends IllegalFunctionArgumentException {
    public IllegalLogArgumentException(String message, int a) {
        super(message, "log", a);
    }
}
