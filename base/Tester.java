package base;

import java.util.Locale;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public abstract class Tester {
    protected final TestCounter counter;

    protected Tester(final TestCounter counter) {
        this.counter = counter;

        Locale.setDefault(Locale.US);
        Asserts.checkAssert(getClass());
    }

    public abstract void test();

    public int mode() {
        return counter.mode();
    }

    public ExtendedRandom random() {
        return counter.random();
    }
}
