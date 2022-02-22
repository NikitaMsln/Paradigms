package expression.exceptions;

public class OverflowConstParseException extends ParserException {
    public OverflowConstParseException(String exp, String message, int pos) {
        super(exp, message, pos);
    }
}
