package expression.exceptions;

public class MissingBracketException extends IllegalSymbolException {
    public MissingBracketException(String exp, String message, int pos) {
        super(exp, message, pos);
    }
}
