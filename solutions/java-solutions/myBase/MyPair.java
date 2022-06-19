package myBase;

public class MyPair<F, S> {
    protected final F first;
    protected final S second;

    public MyPair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("%s %s", first, second);
    }
}
