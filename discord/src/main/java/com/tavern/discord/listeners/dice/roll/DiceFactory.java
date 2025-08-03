package com.tavern.discord.listeners.dice.roll;

import com.tavern.utilities.StringUtils;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiceFactory {
    private static final Pattern DICE_PART = Pattern.compile("^(?<count>\\d+)d(?<sides>\\d+)$");
    private static final Pattern CONSTANT_PART = Pattern.compile("^(?<constant>\\d+)$");

    public DiceExpressionPart parseExpression(String expression) throws IllegalArgumentException {
        if (StringUtils.isNullOrBlank(expression)) {
            throw new IllegalArgumentException("Dice expression cannot be null or blank");
        }

        String expressionNoWhitespace = expression.trim().replaceAll("\\s+", "");

        Stack<DiceExpressionOperator> operators = new Stack<>();
        DiceExpressionOperator.orderedOperators().reversed().forEach(operators::push);

        while (!operators.empty()) {
            DiceExpressionOperator operator = operators.pop();
            String[] parts = expression.split(Pattern.quote(operator.getOperator()));

            if (parts.length == 2) {
                return operator.createOperation(List.of(parseValue(parts[0]), parseValue(parts[1])));
            }
        }

        return parseValue(expressionNoWhitespace);
    }

    private DiceExpressionValue parseValue(String value) throws IllegalArgumentException {
        if (StringUtils.isNullOrBlank(value)) {
            throw new IllegalArgumentException("Dice expression value cannot be null or blank");
        }

        Matcher dicePartMatcher = DICE_PART.matcher(value);
        if (dicePartMatcher.matches()) {
            int count, sides;

            try {
                count = Integer.parseInt(dicePartMatcher.group("count"));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Dice count must be an integer", ex);
            }

            try {
                sides = Integer.parseInt(dicePartMatcher.group("sides"));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Dice sides must be an integer", ex);
            }

            return dice(count, sides);
        }

        Matcher constantPartMatcher = CONSTANT_PART.matcher(value);
        if (constantPartMatcher.matches()) {
            try {
                return constant(Integer.parseInt(constantPartMatcher.group("constant")));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Constant must be an integer", ex);
            }
        }

        throw new IllegalArgumentException("Invalid dice expression value: " + value);
    }

    public DiceExpressionValue dice(int count, int sides) {
        return new Dice(count, sides);
    }

    public DiceExpressionValue constant(int constant) {
        return new Constant(constant);
    }

}
