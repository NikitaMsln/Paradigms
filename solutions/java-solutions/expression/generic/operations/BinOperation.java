package expression.generic.operations;

public interface BinOperation<T> extends Operation<T> {
    T makeOp(T x, T y);
}
