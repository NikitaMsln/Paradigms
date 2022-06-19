package expression.generic;

import expression.generic.executors.BigIntegerExecutor;
import expression.generic.executors.DoubleExecutor;
import expression.generic.executors.Executor;
import expression.generic.executors.IntegerExecutor;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(new GenericTabulator().tabulate("l", "-    (2+2)", 0, 0, 0, 0, 0, 0)[0][0][0]);
    }

    private static class Test {
        private final static Map<String, Executor<?>> modeToExecutor = Map.of(
                "i", new IntegerExecutor(true),
                "d", new DoubleExecutor(),
                "bi", new BigIntegerExecutor()
        );

        private Executor<?> getExecutor(String mode) {
            return modeToExecutor.get(mode);
        }

        public void main(String mode) {
            give(getExecutor(mode));
        }

        private <T> void give(Executor<T> executor) {
            System.out.println(executor.parseConst("1"));
        }
    }
}
