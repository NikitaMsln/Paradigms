package expression.generic.operations;

import expression.generic.executors.Executor;

public abstract class AbstractExpression<T> implements CommonExp<T> {
    /**
     * <b>getPriority()</b>           <br/>
     *  {@code 10000    VarConst    } <br/>
     *  {@code 900..999 abs l0 t0 - } <br/>
     *  {@code 700..799 ** //       } <br/>
     *  {@code 500..599 / *         } <br/>
     *  {@code 300..399 + -         } <br/>
     *  {@code 100..199 >> << >>>   }
     */
    private final StringBuilder toStr = new StringBuilder();
    private final StringBuilder toMiniStr = new StringBuilder();
    protected final Executor<T> executor;

    protected AbstractExpression(Executor<T> executor) {
        this.executor = executor;
    }

    @Override
    public Executor<T> getExecutor() {
        return executor;
    }

    @Override
    public String toString() {
        if (toStr.isEmpty()) {
            toString(toStr);
        }
        return toStr.toString();
    }

    @Override
    public String toMiniString() {
        if (toMiniStr.isEmpty()) {
            toMiniString(toMiniStr);
        }
        return toMiniStr.toString();
    }
}
