package myBase;

import java.util.Comparator;
import java.util.Objects;

public class StringComparator {
    public static final Comparator<String> comparator =
            (s1, s2) -> s1.length() == s2.length() ? s1.compareTo(s2) : Integer.compare(s1.length(), s2.length());

    public static int compare(String a, String b) {
        return Objects.compare(a, b, comparator);
    }

    public static int compareInt(String a, String b) {
        int x = sgn(a);
        if (sgn(a) == sgn(b)) {
            return x * compare(a, b);
        }
        return x;
    }

    public static int sgn(String a) {
        return a.startsWith("-") ? -1 : 1;
    }

    public static boolean isIntegerValue(String a) {
        return compareInt(a, Integer.MAX_VALUE + "") <= 0 && compareInt(a, Integer.MIN_VALUE + "") >= 0;
    }
}
