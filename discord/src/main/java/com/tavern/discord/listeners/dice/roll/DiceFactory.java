package com.tavern.discord.listeners.dice.roll;

import com.tavern.utilities.StringUtils;

import java.util.regex.Pattern;

public class DiceFactory {

    public DiceExpression parseExpression(String expression) throws IllegalArgumentException {
        if (StringUtils.isNullOrBlank(expression)) {
            throw new IllegalArgumentException("Dice expression cannot be null or blank");
        }

        DiceExpressionTokenizer tokenizer = new DiceExpressionTokenizer(expression);
        return parseAddSub(tokenizer, false);
    }

    private DiceExpression parseAddSub(DiceExpressionTokenizer tokenizer, boolean isNested) {
        AddAndSubtractExpression.Builder addBuilder = AddAndSubtractExpression.builder();

        char operator = DiceExpressionTokenizer.Operator.ADD;
        DiceExpression left;

        DiceExpressionTokenizer.Token token = tokenizer.nextToken();
        if (DiceExpressionTokenizer.Token.CONSTANT == token) {
            left = tokenizer.valueAsConstant();
        } else if (DiceExpressionTokenizer.Token.DICE == token) {
            left = tokenizer.valueAsDice();
        } else if (DiceExpressionTokenizer.Token.PARENTHESIS_OPEN == token) {
            left = new ParenthesisExpression(parseAddSub(tokenizer, true));
        } else {
            throw new IllegalArgumentException("Invalid dice expression, must start with constant or dice token");
        }

        while (DiceExpressionTokenizer.Token.END != tokenizer.nextToken()) {
            token = tokenizer.token();
            switch (token) {
                case OPERATOR -> {
                    char newOperator = tokenizer.valueAsOperator();
                    if (newOperator == DiceExpressionTokenizer.Operator.ADD) {
                        addBuilder.add(left);
                        left = null;
                        operator = newOperator;
                    } else if (newOperator == DiceExpressionTokenizer.Operator.SUBTRACT) {
                        addBuilder.subtract(left);
                        left = null;
                        operator = newOperator;
                    } else if (newOperator == DiceExpressionTokenizer.Operator.MULTIPLY) {
                        left = parseMult(tokenizer, left);
                    }
                }
                case CONSTANT -> {
                    if (null == left) {
                        left = tokenizer.valueAsConstant();
                    } else {
                        throw new IllegalArgumentException("Invalid dice expression, unexpected constant");
                    }
                }
                case DICE -> {
                    if (null == left) {
                        left = tokenizer.valueAsDice();
                    } else {
                        throw new IllegalArgumentException("Invalid dice expression, unexpected dice");
                    }
                }
                case PARENTHESIS_OPEN -> {
                    if (null == left) {
                        left = new ParenthesisExpression(parseAddSub(tokenizer, true));
                    } else {
                        throw new IllegalArgumentException("Invalid dice expression, unexpected open parenthesis");
                    }
                }
                case PARENTHESIS_CLOSE -> {
                    if (!isNested) {
                        throw new IllegalArgumentException("Invalid dice expression, unmatched parenthesis");
                    }
                    if (null != left) {
                        if (DiceExpressionTokenizer.Operator.ADD == operator) {
                            addBuilder.add(left);
                        } else  { // SUBTRACT
                            addBuilder.subtract(left);
                        }
                    }
                    return addBuilder.build();
                }
                default -> throw new IllegalArgumentException("Invalid dice expression, unexpected token: " + token);
            }
        }

//        if (isNested) {
//            throw new IllegalArgumentException("Invalid dice expression, unmatched parenthesis");
//        }

        if (null != left) {
            if (DiceExpressionTokenizer.Operator.ADD == operator) {
                addBuilder.add(left);
            } else  { // SUBTRACT
                addBuilder.subtract(left);
            }
        }
        return addBuilder.build();
    }

    private DiceExpression parseMult(DiceExpressionTokenizer tokenizer, DiceExpression left) {
        MultiplyExpression.Builder multiplyBuilder = MultiplyExpression.builder()
            .multiply(left);

        while (DiceExpressionTokenizer.Token.END != tokenizer.nextToken()) {
            switch (tokenizer.token()) {
                case OPERATOR -> {
                    // Always multiplier, no work needed
                }
                case CONSTANT -> multiplyBuilder.multiply(tokenizer.valueAsConstant());
                case DICE -> multiplyBuilder.multiply(tokenizer.valueAsDice());
                case PARENTHESIS_OPEN -> multiplyBuilder.multiply(new ParenthesisExpression(parseAddSub(tokenizer, true)));
                case PARENTHESIS_CLOSE -> multiplyBuilder.build();
                default -> throw new IllegalArgumentException("Invalid dice expression, unexpected token: " + tokenizer.token());
            }

            // Peek ahead for [+-], if so, break and return
            DiceExpressionTokenizer.TokenAndValue peeked = tokenizer.peek();
            if (DiceExpressionTokenizer.Token.OPERATOR == peeked.token() && peeked.value().charAt(0) != DiceExpressionTokenizer.Operator.MULTIPLY) {
                break;
            }
        }

        return multiplyBuilder.build();
    }

    public DiceExpressionValue dice(int count, int sides) {
        return new Dice(count, sides);
    }

    public DiceExpressionValue constant(int constant) {
        return new Constant(constant);
    }

}
