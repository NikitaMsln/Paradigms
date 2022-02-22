package expression.exceptions;

public class ParserException extends Exception {
    public ParserException(String exp, String message, int pos) {
        super(String.format(
                "Parser error at position %d ('%c') while parsing \"%s\": %s",
                pos, exp.charAt(pos - 1), exp, message
        ));
    }
}
