package expression.exceptions;

import expression.*;

import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

public class Main {
//    public static void myBase.main(String[] args) {
//        Comparator<String> comparator = (s1, s2) -> s1.length() == s2.length() ? s1.compareTo(s2) : Integer.compare(s1.length(), s2.length());
//        String a = "684532";
//        System.out.println(Objects.compare(a, "" + Integer.MAX_VALUE, comparator));
//        System.out.println(a.compareTo("" + Integer.MAX_VALUE));
//        CheckedMultiply d = new CheckedMultiply(new Variable("x"), new Variable("y"));
//        System.out.println(d.evaluate(Integer.MIN_VALUE, -1, 0));
//        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE - 1; i++) {
//            for (int j = Integer.MIN_VALUE; j <= Integer.MAX_VALUE - 1; j++) {
//                boolean overflow1 = false, overflow2 = false;
//                if ((long) i * (long) j != (long) (i * j)) {
//                    overflow1 = true;
//                }
//                try {
//                    d.evaluate(i, j, 0);
//                } catch (OverflowException e) {
//                    overflow2 = true;
//                }
//                if (overflow1 != overflow2) {
//                    System.out.printf("%d %d: %s %s\n", i, j, overflow1, overflow2);
//                    System.exit(0);
//                }
//            }
//        }
//    }

//    private int sgn(boolean f) {
//        return f ? 1 : -1;
//    }

    public static void main(String[] args) throws ParserException {
        StringCharSource str = new StringCharSource("text");
//        if (!str.take("text")) {
//            throw new AssertionError("fail");
//        }

        BaseParser parser = new BaseParser(str);
        System.out.println(parser.take("tex"));
        System.out.println(parser.test('t'));
        System.out.println(parser.take('t'));
//        throw error(IllegalFunctionArgumentException::new, "d");
    }

    private static ExpressionException error(Function<String, ExpressionException> b, String message) {
        Function<Function<String, ExpressionException>, ExpressionException> a = q -> q.apply(message);
        return a.apply(b);
    }
}