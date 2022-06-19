package expression.exceptions;

public class IllegalSymbolException extends ParserException {
    public IllegalSymbolException(String exp, String message, int pos) {
        super(exp, message, pos);
    }
}
