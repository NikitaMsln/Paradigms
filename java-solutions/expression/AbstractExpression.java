package expression;

public abstract class AbstractExpression implements CommonExp {
    /**
     * getPriority()
     *    10000    VarConst
     *  9 900..999 abs l0 t0 -
     *  7 700..799 ** //
     *  5 500..599 / *
     *  3 300..399 + -
     *  1 100..199 >> << >>>
     */
    private final StringBuilder toStr = new StringBuilder();
    private final StringBuilder toMiniStr = new StringBuilder();

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
