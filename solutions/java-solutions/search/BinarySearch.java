package search;

public class BinarySearch {
    /**
     * <h3> Контракт функции {@link search.BinarySearch#main(String[])} </h3>
     * Pred: {@code args.length > 0 && i in [Int.MIN_VALUE, Int.MAX_VALUE] for i in args && args[j] <= args[j - 1] for j in [2, args.length)}
     * <br/>
     * Post: {@code min { d in [0, args.length) | args[d] <= x } (is printed to the System.out)}
     * @param args array of strings
     */
    public static void main(String[] args) {
        /** Будем считать, что в любом массиве a выполнено a[-1] == +inf && a[len(a)] == -inf */
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        System.out.println(binSearchIterative(a, x));
//        System.out.println(binSearchRecursive(a, x));
    }

    /**
     * <h3> Контракт функции {@link search.BinarySearch#binSearchIterative(int[], int)} </h3>
     * Pred: {@code a[i] <= a[i - 1] for i in [0, len(a)]}
     * <br/>
     * Post: {@code R == min { r in [0, len(a)] : a[r] <= x }}
     * @param a array of integers
     * @param x integer
     * @return R
     */
    public static int binSearchIterative(final int[] a, final int x) {
        /** a[i] <= a[i - 1] for i in [0, len(a)] */
        int l = -1, r = a.length;
        /** l == -1 && r == a.length */
        /** a[r] <= x < a[l] && -1 <= l < r <= len(a) -- инвариант цикла */
        while (r > l + 1) {
            /** a[r] <= x < a[l] && -1 <= l < r <= len(a) */
            int m = (l + r) / 2;
            /** l < m == (l + r) / 2 < r
                    Докажем, что l < m
             *  l + r - (l + r) % 2 == m * 2 >? l * 2
             *  r - l > 1 >= (l + r) % 2
                    Докажем, что r > m
             *  l + r - (l + r) % 2 == m * 2 < ? r * 2
             *  l - r < -1 < 0 <= (l + r) % 2
            */
            if (a[m] > x) {
                /** a[m] > x */
                l = m;
                /** a[r'] <= x < a[l'] */
            } else {
                /** a[m] <= x */
                r = m;
                /** a[r'] <= x < a[l'] */
            }
            /** a[r'] <= x < a[l'] && -1 < l' < r' <= len(a)
                    Докажем, что -1 < l'
             * l' == l || l' == m > l
             * l' >= l > -1
             * l' > -1
                    Докажем, что r' <= len(a)
             * r' == r || r' == m < r
             * r' <= r <= len(a)
             * r' <= len(a)
            */
        }
        /** a[r'] <= x < a[l'] && -1 < l' < r' <= l' + 1 <= len(a)
         *  a[r'] <= x < a[l'] && 0 <= r' == l' + 1 <= len(a)
         *  a[r'] <= x < a[r' - 1] && 0 <= r' <= len(a)
         *  r' == min { r in [0, len(a)] : a[r] <= x }
        */
        return r;
        /** R == min { r in [0, len(a)] : a[r] <= x } */
    }

    /**
     * <h3> Контракт функции {@link search.BinarySearch#binSearchRecursive(int[], int)} </h3>
     * Pred: {@code a[i] <= a[i - 1] for i in [0, len(a)]}
     * <br/>
     * Post: {@code R == min { t in [0, len(a)] : a[t] <= x }}
     * @param a array of integers
     * @param x integer
     * @return R
     */
    public static int binSearchRecursive(final int[] a, final int x) {
        /** a[i] <= a[i - 1] for i in [0, len(a)] && l == -1 && r == len(a) && a[r] == -inf < x < +inf == a[l] */
        return binSearchRecursive(a, x, -1, a.length);
        /** R == min { t in [-1, len(a)] ⋂ [0, len(a)] : a[t] <= x } */
        /** R == min { t in [0, len(a)] : a[t] <= x } */
    }

    /**
     * <h3> Контракт функции {@link search.BinarySearch#binSearchRecursive(int[], int, int, int)} </h3>
     * Pred: {@code a[i] <= a[i - 1] for i in [0, len(a)] && -1 <= l < r <= len(a) && a[r] <= x < a[l]}
     * <br/>
     * Post: {@code R == min { t in [l, r] ⋂ [0, len(a)] : a[t] <= x }}
     * @param a array of integers
     * @param x integer
     * @param l integer, left border of the segment
     * @param r integer, right border of the segment
     * @return R
     */
    private static int binSearchRecursive(final int[] a, final int x, int l, int r) {
        /** a[i] <= a[i - 1] for i in [0, len(a)] && -1 <= l < r <= len(a) && a[r] <= x < a[l] */
        if (r <= l + 1) {
            /**
             * l < r && r <= l + 1 && a[r] <= x < a[l] && -1 <= l < r <= len(a)
             * r == l + 1 && 0 <= r == l + 1 <= len(a) && a[r] <= x < a[l]
             * -1 <= l < r <= len(a) && a[r] <= x < a[r - 1]
             * r = min { t in [l, r] ⋂ [0, len(a)] : a[t] <= x }
            */
            return r;
            /** R = min { t in [l, r] ⋂ [0, len(a)] : a[t] <= x } */
        }
        /** a[i] <= a[i - 1] for i in [0, len(a)] && 0 <= l + 1 < r <= len(a) && a[r] <= x < a[l] */
        int m = (l + r) / 2;
        /** l <= m == (l + r) / 2 < r */
        if (a[m] > x) {
            /** a[m] > x */
            l = m;
            /** a[r'] <= x < a[l'] */
        } else {
            /** a[m] <= x */
            r = m;
            /** a[r'] <= x < a[l'] */
        }
        /** a[r'] <= x < a[l'] && -1 <= l <= l' < r' <= r <= len(a) && a[i] <= a[i - 1] for i in [0, len(a)] */
        return binSearchRecursive(a, x, l, r);
        /**
         * R == min { t in [l', r'] ⋂ [0, len(a)] : a[t] <= x } && [l', r'] ⊂ [l, r]
         * a[t] > x for t in [l', R - 1] && a[t] <= x for t in [R, r'] && [l', r'] ⊂ [l, r]
         * a[R - 1] > x && a[R] <= x && R in [l', r'] ⊂ [l, r]
         * a[t] >= a[R - 1] > x for t in [l, R - 1] && a[t] <= a[R] <= x for x in [R, r]
         * R == min { t in [l, r] ⋂ [0, len(a)] : a[t] <= x }
        */
    }
}
