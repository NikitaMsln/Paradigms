package expression.generic;

import expression.exceptions.ExpressionException;
import expression.exceptions.ParserException;
import expression.generic.executors.*;
import expression.generic.operations.TripleExpression;
import expression.generic.parser.ExpressionParser;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private final static Map<String, Executor<?>> modeToExecutor = Map.of(
            "i", new IntegerExecutor(true),
            "d", new DoubleExecutor(),
            "bi", new BigIntegerExecutor(),
            "u", new IntegerExecutor(false),
            "l", new LongExecutor(),
            "t", new TruncIntegerExecutor(false)
    );

    private Executor<?> getExecutor(String mode) {
        return modeToExecutor.get(mode);
    }

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2)
            throws Exception {
        return getTable(getExecutor(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] getTable(
            Executor<T> executor, String expression,
            int x1, int x2, int y1, int y2, int z1, int z2
    ) throws ParserException {
        TripleExpression<T> expr = new ExpressionParser<T>().parse(executor, expression);
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        result[i - x1][j - y1][k - z1] = expr.evaluate(
                                executor.parseConst(i),
                                executor.parseConst(j),
                                executor.parseConst(k)
                        );
                    } catch (ExpressionException e) {
                        result[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return result;
    }
}
