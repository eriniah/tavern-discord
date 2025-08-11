package com.tavern.discord.listeners.dice.roll;

import com.google.common.collect.Lists;
import com.tavern.utilities.StringUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiceFactory {
    private static final Pattern DICE_PART = Pattern.compile("^(?<count>\\d+)d(?<sides>\\d+)$");
    private static final Pattern CONSTANT_PART = Pattern.compile("^(?<constant>\\d+)$");

    /**
     * Match first expression, then match pairs of operator + expression
     */
    private static final Pattern ADD_EXPR = Pattern.compile("(^\\s*(?<first>[^+-]+)|\\s*(?<op>[+-]+)\\s*(?<expr>[^+-]+))");


    public DiceExpression parseExpression(String expression) throws IllegalArgumentException {
        if (StringUtils.isNullOrBlank(expression)) {
            throw new IllegalArgumentException("Dice expression cannot be null or blank");
        }

        String expressionNoWhitespace = expression.trim().replaceAll("\\s+", "");
        NestedExprToken tokenizedExpr = NestedExprToken.parse(expression);
        return parseAddExpr(tokenizedExpr);
    }

    private DiceExpression parseAddExpr(NestedExprToken expression) {
        AddAndSubtractExpression.Builder builder = AddAndSubtractExpression.builder();

        Matcher matcher = ADD_EXPR.matcher(expression.expression());
        while (matcher.find()) {
            if (matcher.group("first") != null) {

            }
        }
        
        builder.build();
    }

    private DiceExpression parseMultExpr(String expression) {

    }

    private DiceExpression parseParenthesisExpr(String expression) {

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


    private record NestedExprToken(String expression, List<NestedExprToken> children) {

        static NestedExprToken parse(String expression) {
            return parse(new StringCharacterIterator(expression));
        }

        private static NestedExprToken parse(CharacterIterator expression) {
            if (null == expression || CharacterIterator.DONE == expression.getEndIndex()) {
                throw new IllegalArgumentException("Empty expression");
            }
            boolean isRoot = expression.getIndex() == 0;

            List<NestedExprToken> children = new LinkedList<>();
            StringBuilder buffer = new StringBuilder();
            while (CharacterIterator.DONE != expression.getEndIndex()) {
                char c = expression.next();
                if (Character.isWhitespace(c)) {
                    continue;
                }

                if (c == '(') {
                    buffer.append(String.format("$%d", children.size()));
                    children.add(parse(expression));
                } else if (c == ')') {
                    if (isRoot) {
                        throw new IllegalArgumentException("Invalid dice expression, unmatched parenthesis");
                    }
                    if (buffer.isEmpty()) {
                        throw new IllegalArgumentException("Invalid dice expression, empty parenthesis");
                    }
                    return new NestedExprToken(buffer.toString(), children);
                } else {
                    buffer.append(c);
                }
            }

            return new NestedExprToken(buffer.toString(), children);
        }

    }

}
