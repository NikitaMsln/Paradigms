package expression.exceptions;

public class StringCharSource implements CharSource {
    private final String string;
    private int pos;

    public StringCharSource(String string) {
        this.string = string;
    }

    @Override
    public char next() {
        return string.charAt(pos++);
    }

    @Override
    public int pos() {
        return pos;
    }

    @Override
    public boolean hasNext() {
        return pos < string.length();
    }

    @Override
    public boolean test(String test) {
        for (int i = 0; i < test.length(); i++) {
            if (pos + i - 1 >= string.length() || string.charAt(pos + i - 1) != test.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ParserException error(String message) {
        return new IllegalSymbolException(string, String.format(
                "%s (rest of input: %s)", message, string.substring(pos)
        ), pos);
    }

    @Override
    public String toString() {
        return string;
    }
}
