import java.io.*;
import java.util.*;

public class Main {
    public static class node {
        long val;
        long add = 0;
        long set = 1000000000000000000L + 100;
    }

    static node[] y = new node[4 * 2000001];
    static long[] hg = new long[2000001];

    static void push(int v, int tree_left, int tree_right) {
        if (tree_left == tree_right) {
            if (y[v].add != 0) {
                y[v].val += y[v].add;
                y[v].add = 0;
            } else if (y[v].set != 1000000000000000000L + 100) {
                y[v].val = y[v].set;
                y[v].set = 1000000000000000000L + 100;
            }
            return;
        }
        if (y[v].add != 0) {
            if (y[v * 2 + 1].set != 1000000000000000000L + 100) {
                y[v * 2 + 1].set += y[v].add;
                y[v * 2 + 1].add = 0;
            } else if (y[v * 2 + 1].add != 0) {
                y[v * 2 + 1].add += y[v].add;
                y[v * 2 + 1].set = 1000000000000000000L + 100;
            } else if (y[v * 2 + 1].add == 0 && y[v * 2 + 1].set == 1000000000000000000L + 100) {
                y[v * 2 + 1].add = y[v].add;
                y[v * 2 + 1].set = 1000000000000000000L + 100;
            }

            if (y[v * 2 + 2].set != 1000000000000000000L + 100) {
                y[v * 2 + 2].set += y[v].add;
                y[v * 2 + 2].add = 0;
            } else if (y[v * 2 + 2].add != 0) {
                y[v * 2 + 2].add += y[v].add;
                y[v * 2 + 2].set = 1000000000000000000L + 100;
            } else if (y[v * 2 + 2].add == 0 && y[v * 2 + 2].set == 1000000000000000000L + 100) {
                y[v * 2 + 2].add = y[v].add;
                y[v * 2 + 2].set = 1000000000000000000L + 100;
            }

            y[v].val += y[v].add;
            y[v].add = 0;
            y[v].set = 1000000000000000000L + 100;
            return;
        }

        if (y[v].set != 1000000000000000000L + 100) {
            y[v * 2 + 1].set = y[v].set;
            y[v * 2 + 2].set = y[v].set;
            y[v * 2 + 1].add = 0;
            y[v * 2 + 2].add = 0;

            y[v].val = y[v].set;
            y[v].set = 1000000000000000000L + 100;
            y[v].add = 0;
            return;
        }
    }

    static void b(long[] a, int l, int r, int v) {
        if (r - l == 1) {
            y[v].val = a[l];
            return;
        }
        int mid = (l + r) / 2;
        b(a, l, mid, v * 2 + 1);
        b(a, mid, r, v * 2 + 2);
        y[v].val = Math.min(y[v * 2 + 1].val, y[v * 2 + 2].val);
    }

    static long q(int v, int l, int r, int tl, int tr) {
        push(v, tl, tr);
        if (l >= tr || tl >= r) {
            return 1000000000000000000L + 100;
        }
        if (tl >= l && tr <= r) {
            return y[v].val;
        }
        int mid = (tl + tr) / 2;
//    push(v, tree_left, tr);
        return Math.min(q(v * 2 + 1, l, r, tl, mid),
                q(v * 2 + 2, l, r, mid, tr));
    }

    static void set(int v, int l, int r, long sv, int tl, int tr) {
        push(v, tl, tr);

        if (tl >= l && tr <= r) {
            y[v].set = sv;
            y[v].add = 0;
            push(v, tl, tr);
            return;
        }

        if (tl >= r || tr <= l) {
            return;
        }

        int md = (tl + tr) / 2;
        set(v * 2 + 1, l, r, sv, tl, md);
        set(v * 2 + 2, l, r, sv, md, tr);
        y[v].val = Math.min(y[v * 2 + 1].val, y[v * 2 + 2].val);
    }


    static void plus(int v, int l, int r, long add_value, int tl, int tr) {
        push(v, tl, tr);

        if (tl >= l && tr <= r) {
            y[v].add += add_value;
            y[v].set = 1000000000000000000L + 100;
            ;
            push(v, tl, tr);
            return;
        }

        if (tl >= r || tr <= l) {
            return;
        }

        int mid = (tl + tr) / 2;
        plus(v * 2 + 1, l, r, add_value, tl, mid);
        plus(v * 2 + 2, l, r, add_value, mid, tr);
        y[v].val = Math.min(y[v * 2 + 1].val, y[v * 2 + 2].val);
    }


    public static void main(String[] args) throws IOException {
        for (int i = 0; i < y.length; i++) {
            y[i] = new node();
        }
        int n;
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();

        for (int i = 0; i < n; ++i) {
            hg[i] = scanner.nextInt();
        }

        b(hg, 0, n, 0);
        String arg;
        scanner.nextLine();
        while (scanner.hasNext()) {
            arg = scanner.next();
            if (Objects.equals(arg, "min")) {
                long l, r;
                l = scanner.nextLong();
                r = scanner.nextLong();
                l--;
                System.out.println(q(0, (int) l, (int) r, 0, n));
            } else if (Objects.equals(arg, "set")) {
                long l, r, v;
                l = scanner.nextLong();
                r = scanner.nextLong();
                v = scanner.nextLong();
                l--;
                set(0, (int) l, (int) r, v, 0, n);
            } else if (Objects.equals(arg, "add")) {
                long l, r, v;
                l = scanner.nextLong();
                r = scanner.nextLong();
                v = scanner.nextLong();
                l--;
                plus(0, (int) l, (int) r, v, 0, n);
            }
        }
    }
}