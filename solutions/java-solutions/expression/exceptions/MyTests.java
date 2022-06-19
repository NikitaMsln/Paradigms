package expression.exceptions;

import expression.TripleExpression;

import java.util.Random;

public class MyTests {
    private static final ExpressionParser parser = new ExpressionParser();
    private static final int N = 100;
    private static int cnt = 0;

    public static void main(String[] args) throws ParserException {
//        System.out.println(parser.parse("string + x"));
        checkParser();
        gkTestsParser();
        toMiniStringCheck();
        System.out.printf("\nPassed %d tests\n", cnt);
    }

    private static void toMiniStringCheck() throws ParserException {
        equal("(x + y) - z", "x + y - z");
        equal("(x - y) - z", "x - y - z");
        equal("(x + y) + z", "x + y + z");
        equal("(x - y) + z", "x - y + z");
        equal("x + (y - z)", "x + y - z");
        equal("x - (y - z)", "x - (y - z)");
        equal("x + (y + z)", "x + y + z");
        equal("x - (y + z)", "x - (y + z)");
        equal("(x * y) / z", "x * y / z");
        equal("(x / y) / z", "x / y / z");
        equal("(x * y) * z", "x * y * z");
        equal("(x / y) * z", "x / y * z");
        equal("x * (y / z)", "x * (y / z)");
        equal("x / (y / z)", "x / (y / z)");
        equal("x * (y * z)", "x * y * z");
        equal("x / (y * z)", "x / (y * z)");
    }

    private static void equal(String parse, String mini) throws ParserException {
        String miniParse = parser.parse(parse).toMiniString();
        if (!miniParse.equals(mini)) {
            throw new AssertionError(String.format(
                    "FAILURE %d: toMiniString isn't equals -- for expression %s, expected: %s\n", ++cnt, miniParse, mini
            ));
        }
        System.out.printf("SUCCESS %d: toMiniString parsed: %s\n", ++cnt, mini);
    }

    private static void gkTestsParser() {
        check("0<+1");
        check("(0\t\r");
        check("(x + x) + (z + x) < ");
        check("(0\t\t\b\b\b\0   ) + 1)");
        check("(0 << 0)", (x, y, z) -> 0);
    }

    public static void checkParser() {
        check("(x))");
        check("[x + y]");
        check("abs(-9)", (x, y, z) -> 9);
        check("x", (x, y, z) -> x);
        check("y", (x, y, z) -> y);
        check("z", (x, y, z) -> z);
        check("x ** 2 * y", (x, y, z) -> x * x * y);
        check("x * 2 ** 2", (x, y, z) -> x * 4);
        check("10", (x, y, z) -> 10);
        check("-x", (x, y, z) -> -x);
        check("--x", (x, y, z) -> x);
        check("2 ** (3 ** 2)", (x, y, z) -> 512);
        check("(2 ** 3) ** 2", (x, y, z) -> 64);
        check("-".repeat(500) + "z", (x, y, z) -> z);
        check("x+2", (x, y, z) -> x + 2);
        check("2-y", (x, y, z) -> 2 - y);
        check("  3*  z  ", (x, y, z) -> 3 * z);
        check("x/  -  2", (x, y, z) -> -x / 2);
        check("x*y+(z-1   )/10", (x, y, z) -> x * y + (z - 1) / 10);
        check("-(-(-\t\t-5 + 16   *x*y) + 1 * z) -(((-11)))", (x, y, z) -> -(-(5 + 16 * x * y) + z) + 11);
        check("" + Integer.MAX_VALUE, (x, y, z) -> Integer.MAX_VALUE);
        check("" + Integer.MIN_VALUE, (x, y, z) -> Integer.MIN_VALUE);
        check("x-y-z", (x, y, z) -> x - y - z);
        check("x--y--z", (x, y, z) -> x + y + z);
        check("x >> y >> \tz", (x, y, z) -> x >> y >> z);
        check("((2+2))-0/(--2)*555", (x, y, z) -> 4);
        check("x-x+y-y+z-(z)", (x, y, z) -> 0);
        check("-(-x)", (x, y, z) -> x);
        check("-x", (x, y, z) -> -x);
        check("-\t\t\tz", (x, y, z) -> -z);
        check("-y+(x+8*-z)", (x, y, z) -> -y + x - 8 * z);
        check("(".repeat(500) + "x + y + (-10*-z)" + ")".repeat(500), (x, y, z) -> x + y + 10 * z);
        check("(x))");
        check("(x - 3)*(y *(-y))+-9/(x-9)", (x, y, z) -> (3 - x) * (y * y) - 9 / (x - 9));
        check("l0 l0 x", (x, y, z) -> Integer.numberOfLeadingZeros(Integer.numberOfLeadingZeros(x)));
        check("t0 t0 \t\t\ty", (x, y, z) -> Integer.numberOfTrailingZeros(Integer.numberOfTrailingZeros(y)));
        check("x << (y << z)", (x, y, z) -> x << (y << z));
        check("t0t01");
        check(")");
        check("(");
        check("(".repeat(200) + ")".repeat(199));
        check("x + y )");
        check("10 // \t\t2", (x, y, z) -> 3);
        check("216 // \t\t6", (x, y, z) -> 3);
        check("2 ** 3", (x, y, z) -> 8);
        check("l0 -2", (x, y, z) -> Integer.numberOfLeadingZeros(-2));
        check("(x - y) *  -z +-8", (x, y, z) -> (y - x) * z - 8);
        check("(x <\t 5)");
        check("+ 9-7");
        check("d + 9");
        check("\t\t\t\t\t2+\t\t".repeat(5) + " 2 \t\t\t** 9", (x, y, z) -> 522);
        check("2 ** 2 ** 2", (x, y, z) -> 16);
        check("abs x // 2", (x, y, z) -> (int) (Math.log(Math.abs(x)) / Math.log(2)));
        check("(".repeat(10) + ")".repeat(10));
    }

    private static void check(String test, Triple e) {
        try {
            TripleExpression expr = parser.parse(test);
            Random random = new Random();
            for (int i = 0; i < N; i++) {
                int x = random.nextInt();
                int y = random.nextInt();
                int z = random.nextInt();
                int evaluate = expr.evaluate(x, y, z);
                int calculate = e.calculate(x, y, z);
                if (evaluate != calculate) {
                    System.err.printf("\nFAILURE %d: %s\n", ++cnt, test);
                    System.err.printf("parsed as: %s\nf(%d, %d, %d) -> [eval = %d, calc = %d]\n\n",
                            expr, x, y, z, evaluate, calculate);
                    return;
                }
            }
            System.out.printf("SUCCESS %d: %s\n", ++cnt, test);
        } catch (OverflowException g) {
            System.out.printf("SUCCESS %d: overflow while evaluating %s\n", ++cnt, test);
        } catch (Exception exc) {
            System.err.printf("\nFAILURE %d: exception in parsing %s\n", ++cnt, test);
            exc.printStackTrace();
        }
    }

    private static void check(String test) {
        try {
            parser.parse(test);
        } catch (ParserException e) {
            System.out.printf("SUCCESS %d: exception in parsing %s\n", ++cnt, test);
            return;
        }
        System.err.printf("\nFAILURE %d: expected exception in parsing %s\n\n", ++cnt, test);
    }

    @FunctionalInterface
    interface Triple {
        int calculate(int x, int y, int z) throws ExpressionException;
    }
}
