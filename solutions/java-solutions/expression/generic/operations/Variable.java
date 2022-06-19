package expression.generic.operations;

import expression.exceptions.ExpressionException;
import expression.generic.executors.Executor;

public class Variable<T> extends AbstractVarConst<T> {
    public Variable(Executor<T> executor, String name) {
        super(executor, name);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (str) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new ExpressionException(String.format("invalid name of variable: %s", str));
        };
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }
}
