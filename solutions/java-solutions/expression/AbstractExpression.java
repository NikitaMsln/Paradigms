package expression;

public abstract class AbstractExpression implements CommonExp {
    /**
     * getPriority()
     *    10000    VarConst     <br>
     *  9 900..999 abs l0 t0 -  <br>
     *  7 700..799 ** //        <br>
     *  5 500..599 / *          <br>
     *  3 300..399 + -          <br>
     *  1 100..199 >> << >>>    <br>
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
