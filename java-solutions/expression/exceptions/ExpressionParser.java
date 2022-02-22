package expression.exceptions;

import expression.*;
import myBase.StringComparator;

public class ExpressionParser implements TripleParser {

    @Override
    public TripleExpression parse(String expression) throws ParserException {
        return new ExprParser(new StringCharSource(expression)).parse();
    }

    private static class ExprParser extends BaseParser {
        private int bal = 0;

        private ExprParser(CharSource source) throws ParserException {
            super(source);
        }

//        private IllegalSymbolException error(error errortype, String message) {
//            IllegalSymbolException::new;
//            return new IllegalSymbolException(source.toString(), message, source.pos());
//        }
//
//        @FunctionalInterface
//        private interface error {
//            IllegalSymbolException error();
//        }

        private CommonExp parse() throws ParserException {
            CommonExp result = parseExpression();
            if (bal > 0) {
//                throw error(MissingBracketException.class, "extra open brackets");
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
         * {@code E -> T1 << ... << T1 | T1 >> ... >> T1 | T1 >>> ... >>> T1 | T1}
         * @return expression
         * @throws ParserException
         */
        private CommonExp parseExpression() throws ParserException {
            skipWhiteSpaces();
            CommonExp left = parseTerm1();
            while (true) {
                skipWhiteSpaces();
                if (take("<<")) {
                    left = new Shift_L(left, parseTerm1());
                } else if (take(">>>")) {
                    left = new Shift_A(left, parseTerm1());
                } else if (take(">>")) {
                    left = new Shift_R(left, parseTerm1());
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
        private CommonExp parseTerm1() throws ParserException {
            skipWhiteSpaces();
            CommonExp left = parseTerm2();
            while (true) {
                skipWhiteSpaces();
                if (take('+')) {
//                    left = cl.get("+").apply(left, parseTerm2());
                    left = new CheckedAdd(left, parseTerm2());
                } else if (take('-')) {
                    left = new CheckedSubtract(left, parseTerm2());
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
        private CommonExp parseTerm2() throws ParserException {
            skipWhiteSpaces();
            CommonExp left = parseTerm3();
            while (true) {
                skipWhiteSpaces();
                if (take('*')) {
                    left = new CheckedMultiply(left, parseTerm3());
                } else if (take('/')) {
                    left = new CheckedDivide(left, parseTerm3());
                } else {
                    break;
                }
            }
            return left;
        }

        /**
         * {@code T3 -> T4 ** ... ** T4 | T4 // ... // T4 | T4}
         * @return term type3 (contains operation with priority <= 2)
         * @throws ParserException
         */
        private CommonExp parseTerm3() throws ParserException {
            skipWhiteSpaces();
            CommonExp left = parseTerm4();
            while (true) {
                skipWhiteSpaces();
                if (take("**")) {
                    left = new CheckedPow(left, parseTerm4());
                } else if (take("//")) {
                    left = new CheckedLog(left, parseTerm4());
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
        private CommonExp parseTerm4() throws ParserException {
            skipWhiteSpaces();
            if (take('-')) {
                if (between('0', '9')) {
                    return parseConst(true);
                }
                return new CheckedNegate(parseTerm4());
            } else if (take("l0")) {
                expectWS();
                return new L_Zeroes(parseTerm4());
            } else if (take("t0")) {
                expectWS();
                return new T_Zeroes(parseTerm4());
            } else if (take("abs")) {
                expectWS();
                return new CheckedAbs(parseTerm4());
            }
            return parseTerm5();
        }

        /**
         * {@code T5 -> (E) | Var | Num}
         * @return term type5 (expression in brackets or VarConst)
         * @throws ParserException
         */
        private CommonExp parseTerm5() throws ParserException {
            skipWhiteSpaces();
            if (take('(')) {
                bal++;
                skipWhiteSpaces();
                CommonExp expr = parseExpression();
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
        private AbstractVarConst parseVarConst() throws ParserException {
            skipWhiteSpaces();
            if (between('x', 'z')) {
                return new Variable(Character.toString(take()));
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
        private Const parseConst(boolean needMinus) throws ParserException {
            StringBuilder sb = new StringBuilder();
            if (needMinus || take('-')) sb.append("-");
            while (between('0', '9')) {
                sb.append(take());
            }
            if (sb.isEmpty()) {
                throw new IllegalSymbolException(source.toString(), "unexpected token", source.pos());
            }
            String num = sb.toString();
            if (!StringComparator.isIntegerValue(num)) {
                throw new OverflowConstParseException(
                        source.toString(), "overflow while parsing const value", source.pos()
                );
            }
            return new Const(Integer.parseInt(num));
        }
    }
}