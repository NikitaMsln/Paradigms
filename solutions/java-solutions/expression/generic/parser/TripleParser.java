package expression.generic.parser;

import expression.generic.executors.Executor;
import expression.generic.operations.TripleExpression;

@FunctionalInterface
public interface TripleParser<T> {
    TripleExpression<T> parse(Executor<T> executor, String expression) throws Exception;
}
