package myBase;

public class MyTriple<F, S, T> {
    private final F first;
    private final S second;
    private final T third;

    public MyTriple(final F first, final S second, final T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", first, second, third);
    }
}
