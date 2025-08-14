package com.tavern.discord.listeners.dice.roll

import spock.lang.Specification;

class DiceFactoryTest extends Specification {
    private final Random NOT_RANDOM = Random.from(new ConstantRandomGenerator(2))

    def "parseExpression parses single constant correctly"() {
        given:
        String expression = "5"
        DiceFactory diceFactory = new DiceFactory()

        when:
        def result = diceFactory.parseExpression(expression)

        then:
        result != null
        result.getRepresentation(DiceExpressionValueFormatter.getDefault()) == "5"
        result.evaluate(NOT_RANDOM) == 5
    }

    def "parseExpression parses single dice correctly"() {
        given:
        String expression = "1d6"
        DiceFactory diceFactory = new DiceFactory()

        when:
        def result = diceFactory.parseExpression(expression)

        then:
        result != null
        result.getRepresentation(DiceExpressionValueFormatter.getDefault()) == "1d6"
        result.evaluate(NOT_RANDOM) == 2
    }

    def "parseExpression parses addition correctly"() {
        given:
        String expression = "4 + 2"
        DiceFactory diceFactory = new DiceFactory()

        when:
        def result = diceFactory.parseExpression(expression)

        then:
        result != null
        result.getRepresentation(DiceExpressionValueFormatter.getDefault()) == "4 + 2"
        result.evaluate(NOT_RANDOM) == 6
    }

    def "parseExpression parses subtraction correctly"() {
        given:
        String expression = "9 - 3"
        DiceFactory diceFactory = new DiceFactory()

        when:
        def result = diceFactory.parseExpression(expression)

        then:
        result != null
        result.getRepresentation(DiceExpressionValueFormatter.getDefault()) == "9 - 3"
        result.evaluate(NOT_RANDOM) == 6
    }

    def "parseExpression parses dice and addition correctly"() {
        given:
        String expression = "2d6 + 3"
        DiceFactory diceFactory = new DiceFactory()

        when:
        def result = diceFactory.parseExpression(expression)

        then:
        result != null
        result.getRepresentation(DiceExpressionValueFormatter.getDefault()) == "2d6 + 3"
        result.evaluate(NOT_RANDOM) == 7
    }

    def "parseExpression parses nested parentheses correctly"() {
        given:
        String expression = "2 * (1d6 + 3)"
        DiceFactory diceFactory = new DiceFactory()

        when:
        def result = diceFactory.parseExpression(expression)

        then:
        result != null
        result.getRepresentation(DiceExpressionValueFormatter.getDefault()) == "2 * (1d6 + 3)"
        result.evaluate(NOT_RANDOM) == 2 * 5
    }

    def "parseExpression parses complex expression correctly"() {
        given:
        String expression = "(2d6 + 3) * 2 - 4d8"
        DiceFactory diceFactory = new DiceFactory()

        when:
        def result = diceFactory.parseExpression(expression)

        then:
        result != null
        result.getRepresentation(DiceExpressionValueFormatter.getDefault()) == "(2d6 + 3) * 2 - 4d8"
        result.evaluate(NOT_RANDOM) == (7 * 2) - 8
    }

    def "parseExpression parses another complex expression correctly"() {
        given:
        String expression = "1d4 + 3d8 - (2d4 + 1) + (8 * 1d3)"
        DiceFactory diceFactory = new DiceFactory()

        when:
        def result = diceFactory.parseExpression(expression)

        then:
        result != null
        result.getRepresentation(DiceExpressionValueFormatter.getDefault()) == "1d4 + 3d8 - (2d4 + 1) + (8 * 1d3)"
        result.evaluate(NOT_RANDOM) == 2 + 6 - (4 + 1) + (8 * 2)
    }

    def "parseExpression throws exception for empty string"() {
        given:
        String expression = ""
        DiceFactory diceFactory = new DiceFactory()

        when:
        diceFactory.parseExpression(expression)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Dice expression cannot be null or blank"
    }

    def "parseExpression throws exception for null input"() {
        given:
        String expression = null
        DiceFactory diceFactory = new DiceFactory()

        when:
        diceFactory.parseExpression(expression)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Dice expression cannot be null or blank"
    }

    def "parseExpression throws exception for unmatched parentheses"() {
        given:
        String expression = "(2d6 + 3"
        DiceFactory diceFactory = new DiceFactory()

        when:
        diceFactory.parseExpression(expression)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Invalid dice expression, unmatched parenthesis"
    }

    def "parseExpression throws exception for invalid token"() {
        given:
        String expression = "4 # 3"
        DiceFactory diceFactory = new DiceFactory()

        when:
        diceFactory.parseExpression(expression)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Invalid dice expression, invalid character: #"
    }
}
