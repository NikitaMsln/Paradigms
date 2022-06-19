package expression;

import expression.exceptions.ExpressionException;

public class Variable extends AbstractVarConst {
    public Variable(String name) {
        super(name);
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (str) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new ExpressionException(String.format("invalid name of variable: %s", str));
        };
    }
}
