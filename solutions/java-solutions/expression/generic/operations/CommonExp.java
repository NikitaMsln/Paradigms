package expression.generic.operations;

import expression.generic.executors.Executor;

public interface CommonExp<T> extends TripleExpression<T>, ToMiniString {
    void toString(StringBuilder sb);
    void toMiniString(StringBuilder sb);
    int getPriority();
    Executor<T> getExecutor();
}
