package expression.generic.operations;

@FunctionalInterface
public interface TripleExpression<T> extends ToMiniString {
    T evaluate(T x, T y, T z);
}
