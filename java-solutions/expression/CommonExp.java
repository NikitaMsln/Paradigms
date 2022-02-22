package expression;

public interface CommonExp extends Expression, TripleExpression, ToMiniString {
    void toString(StringBuilder sb);
    void toMiniString(StringBuilder sb);
    int getPriority();
}
