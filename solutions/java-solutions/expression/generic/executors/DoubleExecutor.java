package expression.generic.executors;

public class DoubleExecutor extends AbstractExecutor<Double> {
    @Override
    public Double add(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double multiply(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double divide(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double min(Double x, Double y) {
        return Double.min(x, y);
    }

    @Override
    public Double max(Double x, Double y) {
        return Double.max(x, y);
    }

    @Override
    public Double count(Double x) {
        return (double) Long.bitCount(Double.doubleToLongBits(x));
    }

    @Override
    public Double parseConst(String x) {
        return Double.parseDouble(x);
    }

    @Override
    public Double parseConst(int x) {
        return (double) x;
    }

    @Override
    public int getTypePriority() {
        return 400;
    }
}
