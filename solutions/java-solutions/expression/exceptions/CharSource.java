package expression.exceptions;

public interface CharSource {
    char next();
    boolean hasNext();
    boolean test(String test);
    int pos();
    ParserException error(String message);
    @Override String toString();
}
