package expression.generic.parser;

import expression.exceptions.*;
import expression.generic.executors.Executor;
import expression.generic.operations.*;

import java.util.Map;

public class ExpressionParser<T> implements TripleParser<T> {

    @Override
    public TripleExpression<T> parse(Executor<T> executor, String expression) throws ParserException {
        return new ExprParser<T>(new StringCharSource(expression), executor).parse();
    }

    private static class ExprParser<T> extends BaseParser {
        private int bal = 0;
        private final Executor<T> executor;
        private final Map<String, Variable<T>> vars;

        private ExprParser(CharSource source, Executor<T> executor) {
            super(source);
            this.executor = executor;
            vars = Map.of(
                    "x", new Variable<>(executor, "x"),
                    "y", new Variable<>(executor, "y"),
                    "z", new Variable<>(executor, "z")
            );
        }

        private CommonExp<T> parse() throws ParserException {
            CommonExp<T> result = parseExpression();
            if (bal > 0) {
                throw new MissingBracketException(source.toString(), "extra open bracket", source.pos());
            }
            skipWhiteSpaces();
            if (take(')')) {
                throw new MissingBracketException(source.toString(), "extra close bracket", source.pos());
            }
            if (!take('\0')) {
                throw new IllegalSymbolException(source.toString(), "unexpected char", source.pos());
            }
            return result;
        }

        /**
         * {@code E -> T1 min ... min T1 | T1 max ... max T1 | T1}
         * @return expression
         * @throws ParserException
         */
        private CommonExp<T> parseExpression() throws ParserException {
            skipWhiteSpaces();
            CommonExp<T> left = parseTerm1();
            while (true) {
                skipWhiteSpaces();
                if (take("min")) {
                    left = new Min<>(left, parseTerm1());
                } else if (take("max")) {
                    left = new Max<>(left, parseTerm1());
                } else {
                    break;
                }
            }
            return left;
        }

        /**
         * {@code T1 -> T2 + ... + T2 | T2 - ... - T2 | T2}
         * @return term type1 (contains operation with priority <= 4)
         * @throws ParserException
         */
        private CommonExp<T> parseTerm1() throws ParserException {
            skipWhiteSpaces();
            CommonExp<T> left = parseTerm2();
            while (true) {
                skipWhiteSpaces();
                if (take('+')) {
                    left = new Add<>(left, parseTerm2());
                } else if (take('-')) {
                    left = new Subtract<>(left, parseTerm2());
                } else {
                    break;
                }
            }
            return left;
        }

        /**
         * {@code T2 -> T3 * ... * T3 | T3 / ... / T3 | T3}
         * @return term type2 (contains operation with priority <= 3)
         * @throws ParserException
         */
        private CommonExp<T> parseTerm2() throws ParserException {
            skipWhiteSpaces();
            CommonExp<T> left = parseTerm3();
            while (true) {
                skipWhiteSpaces();
                if (take('*')) {
                    left = new Multiply<>(left, parseTerm3());
                } else if (take('/')) {
                    left = new Divide<>(left, parseTerm3());
                } else {
                    break;
                }
            }
            return left;
        }

        /**
         * {@code T4 -> -T5 | l0 T5 | t0 T5 | abs T5 | T5}
         * @return term type4 (contains unary operation)
         * @throws ParserException
         */
        private CommonExp<T> parseTerm3() throws ParserException {
            skipWhiteSpaces();
            if (take('-')) {
                if (between('0', '9')) {
                    return parseConst(true);
                }
                return new Negate<>(parseTerm3());
            } else if (take("abs")) {
                expectWS();
                return new Abs<>(parseTerm3());
            } else if (take("count")) {
                return new Count<>(parseTerm3());
            }
            return parseTerm4();
        }

        /**
         * {@code T5 -> (E) | Var | Num}
         * @return term type5 (expression in brackets or VarConst)
         * @throws ParserException
         */
        private CommonExp<T> parseTerm4() throws ParserException {
            skipWhiteSpaces();
            if (take('(')) {
                bal++;
                skipWhiteSpaces();
                CommonExp<T> expr = parseExpression();
                skipWhiteSpaces();
                expect(')');
                bal--;
                return expr;
            }
            return parseVarConst();
        }

        /**
         * {@code Var -> x | y | z}
         * @return variable ({@code 'x', 'y', 'z'}) or const (from {@code Int.MIN..Int.MAX})
         * @throws ParserException
         */
        private AbstractVarConst<T> parseVarConst() throws ParserException {
            skipWhiteSpaces();
            for (String i : vars.keySet()) {
                if (take(i)) {
                    return vars.get(i);
                }
            }
            return parseConst(false);
        }

        /**
         * {@code Const -> digit | digit Const}
         * <br/>
         * {@code digit -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9}
         * @param needMinus if const negative
         * @return const (from {@code Int.MIN..Int.MAX})
         * @throws ParserException
         */
        private Const<T> parseConst(boolean needMinus) throws ParserException {
            StringBuilder sb = new StringBuilder();
            if (needMinus || take('-')) sb.append("-");
            while (between('0', '9')) {
                sb.append(take());
            }
            if (sb.isEmpty()) {
                throw new IllegalSymbolException(source.toString(), "unexpected token", source.pos());
            }
            return new Const<>(executor, sb.toString());
        }
    }
}