package expression.parser;

import expression.TripleExpression;
import expression.exceptions.ExpressionParser;
import expression.exceptions.ParserException;
//import static expression.parser.ExpressionParser.classes;

//import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) throws ParserException {
        TripleExpression e = new ExpressionParser().parse("x*(y//z)");
        System.out.println(e.toString());
        System.out.println(e.toMiniString());
        int x = random.nextInt();
        int y = random.nextInt();
        int z = random.nextInt();
        System.out.printf("f(%d, %d, %d) = %d\n", x, y, z, e.evaluate(x, y, z));
    }

//    public static void main(String[] args)
//            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        CommonExp left = new Const(4), right = new Subtract(new Variable("x"), new Const(5));
//        final Map<String, Constructor<? extends CommonExp>> cl = Map.of(
//                "+", Add.class.getConstructor(CommonExp.class, CommonExp.class)
//        );
//        TripleExpression d = cl.get("+").newInstance(left, right);
//        System.out.println(d);
//    }
}
