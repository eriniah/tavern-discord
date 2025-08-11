package com.tavern.discord.listeners.dice.roll;

final class DiceExpressionTokenizer {
    private final String expression;

    private Token token = Token.START;
    private String value = null;
    private int index = 0;

    DiceExpressionTokenizer(String expression) {
        this.expression = expression;
    }

    public Token nextToken() {
        return next(true).token();
    }

    public TokenAndValue peek() {
        return next(false);
    }

    private TokenAndValue next(boolean commit) {
        Token token = null;
        String value = null;

        int i = index;
        while (i < expression.length()) {
            char c = expression.charAt(i);
            i++;

            if (Character.isWhitespace(c)) {
                continue;
            }

            if (c == '(') {
                token = Token.PARENTHESIS_OPEN;
            } else if (c == ')') {
                token = Token.PARENTHESIS_CLOSE;
            } else if (c == '+' || c == '-' || c == '*') {
                value = "" + c;
                token = Token.OPERATOR;
            } else if (Character.isDigit(c)) {
                StringBuilder valueBuffer = new StringBuilder().append(c);
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    valueBuffer.append(expression.charAt(i));
                    i++;
                }
                if (i < expression.length() && expression.charAt(i) == 'd') {
                    i++;
                    StringBuilder sidesBuffer = new StringBuilder();
                    while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                        sidesBuffer.append(expression.charAt(i));
                        i++;
                    }
                    if (sidesBuffer.isEmpty()) {
                        throw new IllegalArgumentException("Invalid dice expression, missing dice sides");
                    }
                    value = valueBuffer + "d" + sidesBuffer;
                    token = Token.DICE;
                } else {
                    value = valueBuffer.toString();
                    token = Token.CONSTANT;
                }
            } else {
                throw new IllegalArgumentException("Invalid dice expression, invalid character: " + c);
            }

            if (commit) {
                index = i;
                this.token = token;
                this.value = value;
            }
            return new TokenAndValue(token, value);
        }

        token = Token.END;
        if (commit) {
            index = i;
            this.token = token;
            this.value = value;
        }
        return new TokenAndValue(token, value);
    }

    public Token token() {
        return token;
    }

    public String value() {
        return value;
    }

    Constant valueAsConstant() {
        try {
            return new Constant(Integer.parseInt(value));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid dice expression, invalid constant format: " + value, ex);
        }
    }

    char valueAsOperator() {
        if (value.length() != 1) {
            throw new IllegalArgumentException("Invalid dice expression, invalid operator format: " + value);
        }
        return value.charAt(0);
    }

    Dice valueAsDice() {
        String[] parts = value.split("d");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid dice expression, invalid dice format: " + value);
        }

        try {
            return new Dice(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid dice expression, invalid dice format: " + value, ex);
        }
    }

    static class Operator {
        public static final char ADD = '+';
        public static final char SUBTRACT = '-';
        public static final char MULTIPLY = '*';
    }

    enum Token {
        START,
        CONSTANT,
        DICE,
        OPERATOR,
        PARENTHESIS_OPEN,
        PARENTHESIS_CLOSE,
        END,
    }

    record TokenAndValue(Token token, String value) {}

}
