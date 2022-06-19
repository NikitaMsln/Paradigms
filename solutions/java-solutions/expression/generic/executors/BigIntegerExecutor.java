package expression.generic.executors;

import java.math.BigInteger;

public class BigIntegerExecutor extends AbstractExecutor<BigInteger>  {
    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger divide(BigInteger x, BigInteger y) {
        if (y.equals(ZERO)) {
            throw division(x, y);
        }
        return x.divide(y);
    }

    @Override
    public BigInteger min(BigInteger x, BigInteger y) {
        return x.min(y);
    }

    @Override
    public BigInteger max(BigInteger x, BigInteger y) {
        return x.max(y);
    }

    @Override
    public BigInteger count(BigInteger x) {
        return parseConst(x.bitCount());
    }

    @Override
    public BigInteger parseConst(String x) {
        return new BigInteger(x);
    }

    @Override
    public BigInteger parseConst(int x) {
        return BigInteger.valueOf(x);
    }

    @Override
    public int getTypePriority() {
        return 300;
    }
}
