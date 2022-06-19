package myBase;

public class MyTriple<F, S, T> {
    protected final F first;
    protected final S second;
    protected final T third;

    public MyTriple(final F first, final S second, final T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public MyTriple(final MyPair<F, S> fp, final T third) {
        this.first = fp.first;
        this.second = fp.second;
        this.third = third;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", first, second, third);
    }
}
