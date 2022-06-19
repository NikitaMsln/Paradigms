package queue;

public class Main {
    public static void main(String[] args) {
        int a = 1;
        int b = 3;
        a = b ^ a ^ (b = a);
        System.out.printf("%d %d", a, b);
    }
}
