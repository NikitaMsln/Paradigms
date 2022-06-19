package expression.generic.operations;

public interface UnOperation<T> extends Operation<T> {
    T makeOp(T x);
}
