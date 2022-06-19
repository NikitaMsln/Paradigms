"use strict"

////////////////////////////////////////////////////////////////////////////////
//                               Expressions                                  //

function makeExpression(expression, toString, prefix, postfix, evaluate, diff) {
    expression.prototype.toString = toString
    expression.prototype.prefix = prefix
    expression.prototype.postfix = postfix
    expression.prototype.evaluate = evaluate
    expression.prototype.diff = diff
}

function Const(val) { this.val = parseFloat(val) }
makeExpression(Const,
    function() { return this.val + '' },
    function() { return this.val + '' },
    function() { return this.val + '' },
    function() { return this.val },
    function() { return VARCONST[0] }
)

function Variable(name) { this.name = name }
const VarToNum = { 'x': 0, 'y': 1, 'z': 2 }
const VARCONST = { '0': new Const(0), '1': new Const(1), '2': new Const(2),
    'x': new Variable('x'), 'y': new Variable('y'), 'z': new Variable('z')}
makeExpression(Variable,
    function() { return this.name },
    function() { return this.name },
    function() { return this.name },
    function(...args) { return args[VarToNum[this.name]] },
    function(diffVar) { return VARCONST[diffVar === this.name ? 1 : 0] }
)

////////////////////////////////////////////////////////////////////////////////
//                               Operations                                   //

function Operation(...exprs) { this.exprs = exprs }
makeExpression(Operation,
    function() { return this.exprs.map(expr => expr.toString()).join(' ') + ' ' + this.operator },
    function() { return '(' + this.operator + ' ' + this.exprs.map(expr => expr.prefix()).join(' ') + ')' },
    function() { return '(' + this.exprs.map(expr => expr.postfix()).join(' ') + ' ' + this.operator + ')' },
    function(x, y, z) { return this.operation_(...this.exprs.map(expr => expr.evaluate(x, y, z))) },
    function(diffVar) { return this.diffOp(diffVar, ...this.exprs) }
)
function makeOperation(operation, operator, diffOp) {
    const op = function(...exprs) { Operation.call(this, ...exprs) }
    op.countOfArgs = operation.length
    op.prototype = Object.create(Operation.prototype)
    op.prototype.operation_ = operation
    op.prototype.operator = operator
    op.prototype.diffOp = diffOp
    return op
}

const Add = makeOperation(
    (a, b) => a + b, '+',
    (diffVar, a, b) => new Add(a.diff(diffVar), b.diff(diffVar))
)
const Subtract = makeOperation(
    (a, b) => a - b, '-',
    (diffVar, a, b) => new Subtract(a.diff(diffVar), b.diff(diffVar))
)
const Multiply = makeOperation(
    (a, b) => a * b, '*',
    (diffVar, a, b) => new Add(new Multiply(a.diff(diffVar), b), new Multiply(a, b.diff(diffVar)))
)
const Divide = makeOperation(
    (a, b) => a / b, '/',
    (diffVar, a, b) => new Divide(
        new Subtract(new Multiply(a.diff(diffVar), b), new Multiply(a, b.diff(diffVar))),
        new Multiply(b, b)
    )
)
const Negate = makeOperation(
    a => -a, 'negate',
    (diffVar, a) => new Negate(a.diff(diffVar))
)
const Gauss = makeOperation(
    (a, b, c, x) => a * Math.pow(Math.E, -(x - b) * (x - b) / c / c / 2), 'gauss',
    (diffVar, a, b, c, x) => new Add(
        new Gauss(a.diff(diffVar), b, c, x),
        new Multiply(new Gauss(a, b, c, x), new Negate(new Divide(
            new Multiply(new Subtract(x, b), new Subtract(x, b)),
            new Multiply(VARCONST[2], new Multiply(c, c))
        )).diff(diffVar))
    )
)
const Sumexp = makeOperation(
    (...args) => args.reduce((res, current) => res + Math.exp(current), 0), 'sumexp',
    (diffVar, ...args) =>
        args.reduce((res, current) => new Add(res, new Multiply(new Sumexp(current), current.diff(diffVar))), VARCONST[0])
)
const Softmax = makeOperation(
    (...args) => Math.exp(args[0]) / args.reduce((res, current) => res + Math.exp(current), 0), 'softmax',
    (diffVar, ...args) => new Multiply(
        new Softmax(...args),
        new Subtract(
            args[0].diff(diffVar),
            new Divide(new Sumexp(...args).diff(diffVar), new Sumexp(...args))
        )
    )
)
const OPS = { '+': Add, '-': Subtract, '*': Multiply, '/': Divide, 'negate': Negate,
    'gauss': Gauss, 'sumexp': Sumexp, 'softmax': Softmax }

////////////////////////////////////////////////////////////////////////////////
//                                  Errors                                    //

function ParserError(message) { this.message = message }
ParserError.prototype = Object.create(Error.prototype)
ParserError.prototype.name = 'ParserError'
ParserError.prototype.constructor = ParserError

function makeError(nameOfError, message) {
    const error = function(...args) { ParserError.call(this, message(...args)) }
    error.prototype = Object.create(ParserError.prototype)
    error.prototype.name = nameOfError
    error.prototype.constructor = error
    return error
}

const UnexpectedTokenError = makeError(
    'UnexpectedTokenError', (pos, expect, found) =>
        `Unexpected token at position ${pos}: expect \'${expect}\', found \'${found}\'`
)
const IllegalCountOfArgumentsError = makeError(
    'IllegalCountOfArgumentsError', (pos, op, expect, found) =>
        `Error at position ${pos}: Illegal count of arguments of function ${op}: expect \'${expect}\', found \'${found}\'`
)
const MissingBracketError = makeError(
    'MissingBracketError', (pos, found) => `Expected ')', but found \'${found}\' at position ${pos}`
)
const IllegalOperationError = makeError(
    'IllegalOperationError', (pos, op) =>
        `Unexpected operation at position ${pos}: ${op} not in OPS`
)
const NumberFormatError = makeError(
    'NumberFormatError', (pos, num) => `Can't parse float at position ${pos}: ${num}`
)
const EmptyExpressionError = makeError('EmptyExpressionError', () => 'Can\'t parse empty string')

////////////////////////////////////////////////////////////////////////////////
//                                 Parsers                                    //

const parseVarConst = token => {
    if (token in VARCONST) return VARCONST[token]
    if (/^-?\d+[.]?\d*$/.test(token.trim())) return new Const(token)
    throw new NumberFormatError(token)
}

const parseOperation = (op, ...args) => {
    if (!(op in OPS)) throw new IllegalOperationError(op)
    if (OPS[op].countOfArgs !== 0 && OPS[op].countOfArgs !== args.length)
        throw new IllegalCountOfArgumentsError(OPS[op].countOfArgs, op, args.length)
    return new OPS[op](...args)
}

const parse = expression => {
    let stack = []
    expression.split(/\s+/).filter(str => str.length > 0).forEach(token =>
        stack.push(token in OPS ? parseOperation(token, ...stack.splice(-OPS[token].countOfArgs)) : parseVarConst(token)))
    return stack[0]
}

const isWS = c => c.trim() === ''

function AbstractParser(source) {
    this._pos = 0
    this._source = source
    this._length = source.length
    this.decPos = (n) => this._pos -= n
    this.take = () => this._source[this._pos++]
    this.isDelim = () => [' ', '(', ')'].includes(this._source[this._pos])
}
AbstractParser.prototype.hasNext = function () {
    return this._pos < this._length
}
AbstractParser.prototype.skipWhitespaces = function () {
    while (this.hasNext() && isWS(this._source[this._pos])) this.take()
}
AbstractParser.prototype.test = function (expect) {
    this.skipWhitespaces()
    if (expect.length > this._length - this._pos) {
        return false
    }
    let ans = true
    for (let i = 0; i < expect.length; i++) {
        ans = ans && (expect[i] === this._source[this._pos + i])
    }
    return ans
}
AbstractParser.prototype.takeToken = function () {
    this.skipWhitespaces()
    if (this.hasNext() && this.isDelim()) {
        return this.take()
    } else {
        let res = ''
        while (this.hasNext() && !(this.isDelim())) {
            res += this.take()
        }
        return res
    }
}
AbstractParser.prototype.testToken = function () {
    let res = this.takeToken()
    this.decPos(res.length)
    return res
}
AbstractParser.prototype.error = function(type, ...args) {
    throw new type(this._pos, ...args)
}

const parse_fix = mode => expression => {
    const parser = new AbstractParser(expression.trim())

    function parseExpression(token) {
        if (token === '(') {
            const res = parseOp()
            token = parser.takeToken()
            if (token !== ')') {
                parser.error(MissingBracketError, token)
            }
            return res
        } else {
            return parseVarConst(token)
        }
    }

    function parseOp() {
        let op
        if (mode === 'pre') {
            op = parser.takeToken()
        }
        const args = []
        while (parser.hasNext() && !parser.test(')') && !(parser.testToken() in OPS)) {
            args.push(parseExpression(parser.takeToken()))
        }
        if (mode === 'post') {
            op = parser.takeToken()
        }
        return parseOperation(op, ...args)
    }

    return function() {
        if (isWS(expression)) {
            throw new EmptyExpressionError()
        }
        const result = parseExpression(parser.takeToken())
        if (parser.hasNext()) {
            parser.error(UnexpectedTokenError, 'end of input', parser.take())
        }
        return result
    }()
}

const parsePrefix = parse_fix('pre')

const parsePostfix = parse_fix('post')

////////////////////////////////////////////////////////////////////////////////
//                                  Tests                                     //

println(parsePrefix('10'))

// println(new Sumexp(new Variable('x')).evaluate(1, 2))
// println(parsePostfix("(x sumexp)"))
println(parsePostfix("10").evaluate(1))
println(Sumexp.countOfArgs)
