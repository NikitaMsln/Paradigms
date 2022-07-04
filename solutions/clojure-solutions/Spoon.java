import java.util.Map;

public class Spoon {
    private int j = 0;
    private final char[] arr = new char[30_000];
    private String input;
    private String program;
    private int pos = 0;
    private int bal = 0;
    private final Map<String, Character> commands = Map.of(
            "1", '+',
            "000", '-',
            "010", '>',
            "011", '<',
            "00100", '[',
            "0011", ']',
            "0010110", ',',
            "001010", '.'
    );

    private StringBuilder toBrainFuckReversed(final String x) {
        if (x.isEmpty()) return new StringBuilder();
        for (Map.Entry<String, Character> command : commands.entrySet()) {
            if (x.startsWith(command.getKey())) {
                return toBrainFuckReversed(x.substring(command.getKey().length())).append(command.getValue());
            }
        }
        throw new IllegalArgumentException("Unknown argument");
    }

    public void runProgram(final String program, final String input, final String mode) {
        this.input = input;
        this.program = mode.equals("bf") ? program : toBrainFuckReversed(program).toString();
        runBf();
    }

    private char at(int i) {
        return program.charAt(i);
    }

    private void runBf() {
        for (int i = 0; i < program.length(); i++) {
            switch (at(i)) {
                case '>' -> j++;
                case '<' -> j--;
                case '+' -> arr[j]++;
                case '-' -> arr[j]--;
                case '.' -> System.out.print(arr[j]);
                case ',' -> arr[j] = input.charAt(pos++);
                case '[' -> {
                    if (arr[j] != 0) continue;
                    while (++bal != 0) {
                        switch (at(++i)) {
                            case '[' -> bal++;
                            case ']' -> bal--;
                        }
                    }
                }
                case ']' -> {
                    if (arr[j] == 0) continue;
                    if (at(i) == ']') bal++;
                    while (--i >= 0 && bal != 0) {
                        if (at(i) == '[') bal--;
                        if (at(i) == ']') bal++;
                    }
                }
            }
        }
    }
}
