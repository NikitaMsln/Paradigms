package expression.exceptions;

public class BaseParser {
    private static final char END = '\0';
    protected final CharSource source;
    protected char ch;

    protected BaseParser(CharSource source) {
        this.source = source;
        take();
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean test(final String expected) {
        return source.test(expected);
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected void expect(final String expected) throws ParserException {
        for (char c : expected.toCharArray()) {
            expect(c);
        }
    }

    protected void expect(final char expected) throws ParserException {
        if (!take(expected)) {
            throw source.error(String.format(
                    "Expected '%s', found '%s'",
                    expected, ch
            ));
        }
    }

    protected boolean take(final String expected) throws ParserException {
        if (test(expected)) {
            expect(expected);
            return true;
        }
        return false;
    }

    protected void skipWhiteSpaces() {
        while (Character.isWhitespace(ch)) take();
    }

    protected void expectWS() throws ParserException {
        if (Character.isWhitespace(ch) || ch == END || ch == '(') {
            skipWhiteSpaces();
        } else {
            throw source.error("whitespace expected");
        }
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
