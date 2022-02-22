package search;

/**
 *
 * @author mavlyut
 */
public class BinarySearchUni {
    /** Будем считать, что в любом массиве a выполнено a[-1] == +inf && a[len(a)] == -inf */

    /**
     * <h3> Контракт функции {@link search.BinarySearchUni#main(String[])} </h3>
     * Pred: {@code for i in [0, len(args)) args[i] is Integer &&
     *      ∃ k in [1, len(args)): args[i] > args[i + 1] for i in [0, k) && args[i] < args[i + 1] for i in [k, len(args))}
     * <br/>
     * Post: {@code k <=> max { d in [1, len(args) - 1] : args[d - 1] >= args[d] } <=>
     *     min { d in [0, len(args) - 1] args[d] < args[d + 1] } }
     * @param args array of strings
     */
    public static void main(String[] args) {
        int[] ints = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            ints[i] = Integer.parseInt(args[i]);
        }
        /**
         * for i in [0, len(args)) args[i] is Integer && ∃ k in [1, len(args)): args[i] > args[i + 1] for i in [0, k)
         *      && args[i] < args[i + 1] for i in [k, len(args))
         */
//        System.out.println(binSearchWithBaseModification(ints));

        /**
         * ∃ k in [1, len(args)): args[i] > args[i + 1] for i in [0, k) && args[i] < args[i + 1] for i in [k, len(args))
         */
//        System.out.println(binSearchUniIterative(ints));
        System.out.println(binSearchUniRecursive(ints));

        /** min { d in [0, len(args) - 1] args[d] < args[d + 1] }
         * (will be printed to System.out)
         */
    }

    /**
     * <h3> Контракт функции {@link search.BinarySearchUni#binSearchWithBaseModification(int[])} </h3>
     * Pred: {@code ∃ k in [1, len(ints)): ints[i] > ints[i + 1] for i in [0, k) && ints[i] < ints[i + 1] for i in [k, len(ints))}
     * <br/>
     * Post: {@code R == min { d in [1, len(ints) - 1] ints[d] < ints[d + 1] } (== k) }
     * @param ints array of integers
     * @return R
     * @see search.BinarySearch#binSearchIterative(int[], int)
     * @see search.BinarySearch#binSearchRecursive(int[], int)
     */
    private static int binSearchWithBaseModification(int[] ints) {
        int[] a = new int[ints.length - 1];
        /** инвариант цикла: for i in [0, len(ints) - 1) a[i] == sgn(ints[i] - ints[i + 1]) in { -1, 0, 1 } in [Int.MIN, Int.MAX] */
        for (int i = 0; i < ints.length - 1; i++) {
            a[i] = sgn(ints[i] - ints[i + 1]);
        }
        /** a[i] == 1 for i in [0, k - 1) && a[k - 1] in { 0, 1 } && a[i] == -1 for i in [k, len(a)] */
        /** a[i] <= a[i - 1] for i in [0, len(a)] */
        /** В терминах новых обозначений:
         *  min { d in [0, len(ints) - 1] ints[d] < ints[d + 1] } <=> min { d in [0, len(a)] : a[d] < 0 } <=>
         *     min { d in [0, len(a)] : a[d] <= -1 }
         */
        return BinarySearch.binSearchRecursive(a, -1);
//        return BinarySearch.binSearchIterative(a, -1);
        /**
         *  R == min { d in [0, len(a)] : a[d] <= -1 }
         *  R == min { d in [0, len(a)] : a[d] < 0 } && a[0] == 1
         *  R == min { d in [1, len(ints) - 1] ints[d] < ints[d + 1] }
         *  R == k
         */
    }

    /**
     * <h3> Контракт функции {@link search.BinarySearchUni#sgn(int)} </h3>
     * Pred: true <br/>
     * Post: {@code R == 1 if x > 0, -1 if x < 0, 0 else}
     * @param x integer
     * @return R
     */
    private static int sgn(int x) {
        if (x == 0) return 0;
        return x / Math.abs(x);
    }

    /**
     * <h3> Контракт функции {@link search.BinarySearchUni#binSearchUniIterative(int[])} </h3>
     * Pred: {@code ∃ k in [1, len(a)): a[i] > a[i + 1] for i in [0, k) && a[i] < a[i + 1] for i in [k, len(a))}
     * <br/>
     * Post: {@code R == min { d in [1, len(a) - 1] a[d] < a[d + 1] } (== k) }
     * @param a array of integers
     * @return R
     */
    private static int binSearchUniIterative(int[] a) {
        /** ∃ k in [1, len(ints)): ints[i] > ints[i + 1] for i in [0, k) && ints[i] < ints[i + 1] for i in [k, len(ints)) */
        int l = -1, r = a.length - 1;
        /** инвариант: a[l] >= a[l + 1] && a[r] < a[r + 1] && -1 <= l < r < a.length && if iter > 0 => l > -1 */
        while (r - l > 1) {
            /** r >= l + 2 <=> r - l >= 2 */
            int m = (l + r) / 2;
            /** m == (l + r) div 2 && l < m < r
             *  m == (l + r - (l + r) % 2) / 2 (настоящее деление)
             *      Докажем, что l < m
             *  l < (l + r - (l + r) % 2) / 2
             *  2 * l < l + r - (l + r) % 2
             *  (l + r) % 2 < r - l
             *  (l + r) % 2 < 2 <= r - l
             *      Докажем, что m < r
             *  (l + r - (l + r) % 2) / 2 < r
             *  l + r - (l + r) % 2 < 2 * r
             *  l - r < (l + r) % 2
             *  l - r <= -2 <= 0 < (l + r) % 2
             */
            if (a[m] < a[m + 1]) {
                r = m;
                /** 0 <= l' == l < m == r' < r < a.length && a[l'] >= a[l' + 1] && a[r'] < a[r' + 1] */
            } else {
                l = m;
                /** 0 <= l < m == l' < r == r' < a.length && a[l'] >= a[l' + 1] && a[r'] < a[r' + 1] */
            }
            /** 0 <= l' < r' < a.length && a[l'] >= a[l' + 1] && a[r'] < a[r' + 1] */
        }
        /**
         * r - l <= 1 && 0 <= l < r < a.length <=> r == l + 1 in [1, len(a))
         * a[l] >= a[l + 1] == a[r] < a[r + 1] => r == min { d in [1, len(a)) : a[d] < a[d + 1] }
         */
        return r;
        /** returned min { d in [1, len(a)) : a[d] < a[d + 1] } == k */
    }

    /**
     * <h3> Контракт функции {@link search.BinarySearchUni#binSearchUniRecursive(int[])} </h3>
     * Pred: {@code ∃ k in [1, len(a)): a[i] > a[i + 1] for i in [0, k) && a[i] < a[i + 1] for i in [k, len(a))}
     * <br/>
     * Post: {@code R == min { d in [1, len(a) - 1] a[d] < a[d + 1] } (== k) }
     * @param a array of integers
     * @return R
     */
    private static int binSearchUniRecursive(int[] a) {
        return binSearchUniRecursive(a, -1, a.length - 1);
        /** returned min { d in [-1, len(a) - 1] ⋂ [1, len(a) - 1] a[d] < a[d + 1] } */
    }

    /**
     * <h3> Контракт функции {@link search.BinarySearchUni#binSearchUniRecursive(int[], int, int)} </h3>
     * Pred: {@code ∃ k in [l, r] ⋂ [1, len(a)): a[i] > a[i + 1] for i in [l, r] ⋂ [0, k) && a[i] < a[i + 1] for i in [l, r] ⋂ [k, len(a)) &&
     * && -1 <= l < r <= len(a) && a[r] <= x < a[l]}
     * <br/>
     * Post: {@code R == min { d in [l, r] ⋂ [1, len(a) - 1] a[d] < a[d + 1] } (== k) }
     * @param a array of integers
     * @param l integer, left border of the segment
     * @param r integer, right border of the segment
     * @return R
     */
    private static int binSearchUniRecursive(int[] a, int l, int r) {
        /** l < r */
        if (r <= l + 1) {
            /**
             * r == l + 1 && a[l] > a[l + 1] == a[r] < a[r + 1] <=>
             *     r == min { d in [1, len(a)) : a[d] < a[d + 1] } (== k)
             */
            return r;
            /** returned ==k */
        }
        /** r - l > 1 && l < r */
        int m = (l + r) / 2;
        /** l < m < r */
        if (a[m] < a[m + 1]) {
            r = m;
            /** 0 <= l' == l < m == r' < r < a.length && a[l'] >= a[l' + 1] && a[r'] < a[r' + 1] */
        } else {
            l = m;
            /** 0 <= l < m == l' < r == r' < a.length && a[l'] >= a[l' + 1] && a[r'] < a[r' + 1] */
        }
        /** 0 <= l' < r' < a.length && a[l'] >= a[l' + 1] && a[r'] < a[r' + 1] */
        return binSearchUniRecursive(a, l, r);
        /** returned min { d in [l', r'] ⋂ [1, len(a) - 1] a[d] < a[d + 1] } && l < l' (< k) (<=> a[l] > a[l'])
         *      && (k < ) r' < r (<=> a[r'] < a[r]) =>
         *          returned min { d in [l, r] ⋂ [1, len(a) - 1] a[d] < a[d + 1] }
         */
    }
}
