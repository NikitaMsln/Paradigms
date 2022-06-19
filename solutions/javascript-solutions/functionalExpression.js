"use strict";

const VarToNum = { 'x': 0, 'y': 1, 'z': 2 }
const cnst = val => () => parseFloat(val)
const variable = name => (...args) => args[VarToNum[name]]
const operation = op => Object.defineProperty(
    (...expressions) => (x, y, z) => op(...expressions.map(expr => expr(x, y, z))),
    'count', { value: op.length }
)
const add = operation((a, b) => a + b)
const subtract = operation((a, b) => a - b)
const multiply = operation((a, b) => a * b)
const divide = operation((a, b) => a / b)
const negate = operation(a => -a)
const avg3 = operation((a, b, c) => (a + b + c) / 3)
const med5 = operation((a, b, c, d, e) => ([a, b, c, d, e].sort((a, b) => (b < a) - (a < b))[2]))
const pi = cnst(Math.PI)
const e = cnst(Math.E)
const VARCONST = { 'pi': pi, 'e': e, 'x': variable('x'), 'y': variable('y'), 'z': variable('z') }
const OPS = {'+': add, '-': subtract, '*': multiply, '/': divide, 'negate': negate,
    'avg3': avg3, 'med5': med5 }

const xi = array => x => x in array
const takeIf = (token, condition, action, ...args) => condition(token) ? action(token) : takeIf(token, ...args)

const parse = expression => (x, y, z) => {
    let stack = []
    expression.split(' ').filter(str => str.length > 0).forEach(token => stack.push(takeIf(token,
        xi(OPS), x => OPS[x](...stack.splice(-OPS[x].count)),
        xi(VARCONST), x => VARCONST[x],
        x => !isNaN(parseFloat(x)), x => cnst(x))
    ))
    return stack[0](x, y, z)
}

// let variableX = variable("x")
// const testExpression1 = add(subtract(multiply(variableX, variableX), multiply(cnst(2), variableX)), cnst(1))
const testExpression = parse('x x * x 2 * - 1 +')
for (let i = 0; i < 11; i++) console.log(testExpression(i))
