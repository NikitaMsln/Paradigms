package expression.generic.operations;

import expression.generic.executors.Executor;

public abstract class AbstractVarConst<T> extends AbstractExpression<T> {
    protected final String str;

    protected AbstractVarConst(Executor<T> executor, String str) {
        super(executor);
        this.str = str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractVarConst exp) {
            return str.equals(exp.str);
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 10000;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(str);
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        sb.append(str);
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }
}