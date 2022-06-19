import java.util.Map;

public class Spoon {
    private int i = 0;
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

    private String toBrainFuck(String x) {
        if (x.isEmpty()) return "";
        for (Map.Entry<String, Character> command : commands.entrySet()) {
            if (x.startsWith(command.getKey())) {
                return command.getValue() + toBrainFuck(x.substring(command.getKey().length()));
            }
        }
        throw new IllegalArgumentException("Unknown argument");
    }

    public void runProgram(String program, String input) {
        this.input = input;
        this.program = toBrainFuck(program);
        System.out.println(this.program);
        run(0);
    }

    private void run(int ind) {
        if (ind >= program.length()) return;
        switch (program.charAt(ind)) {
            case '+' -> arr[i]++;
            case '-' -> arr[i]--;
            case '>' -> i++;
            case '<' -> i--;
            case ',' -> arr[i] = input.charAt(pos++);
            case '.' -> System.out.print(arr[i]);
            case ']' -> bal--;
            case '[' -> {
                int tmpBal = bal, forSt = ind;
                while (arr[i] != 0) {
                    ind = forSt;
                    bal = tmpBal + 1;
                    while (bal != tmpBal) {
                        run(++ind);
                    }
                }
            }
        }
        run(++ind);
    }
}
