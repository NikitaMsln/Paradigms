package expression;

public class Const extends AbstractVarConst {
    private final Number value;

    public Const(Number value) {
        super(value.toString());
        this.value = value;
    }

    public Const(int value) {
        this(Integer.valueOf(value));
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }
}