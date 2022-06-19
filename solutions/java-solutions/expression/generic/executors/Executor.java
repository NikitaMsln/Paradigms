package expression.generic.executors;

public interface Executor<T> {
    Executor<T> max(Executor<T> x);
    /**
     * Type         Priority    <br/>
     *  Int         100         <br/>
     *  Long        200         <br/>
     *  Double      400         <br/>
     *  BigInt      300         <br/>
     */
    int getTypePriority();

    T add(T x, T y);
    T subtract(T x, T y);
    T multiply(T x, T y);
    T divide(T x, T y);
    T min(T x, T y);

    T max(T x, T y);
    T abs(T x);
    T negate(T x);
    T count(T x);
    T parseConst(String x);
    T parseConst(int x);
}
