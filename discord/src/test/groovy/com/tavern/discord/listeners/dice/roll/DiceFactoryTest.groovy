package com.tavern.discord.listeners.dice.roll

import spock.lang.Specification

class DiceFactoryTest extends Specification {

    private DiceFactory diceFactory = new DiceFactory()
    private Random random = Random.from(new ConstantRandomGenerator(2));

    def "parseExpression should throw IllegalArgumentException for null input"() {
        when:
        diceFactory.parseExpression(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "parseExpression should throw IllegalArgumentException for empty input"() {
        when:
        diceFactory.parseExpression("")

        then:
        thrown(IllegalArgumentException)
    }

    def "parseExpression should throw IllegalArgumentException for whitespace input"() {
        when:
        diceFactory.parseExpression("   ")

        then:
        thrown(IllegalArgumentException)
    }

    def "parseExpression should correctly parse valid single dice expression"() {
        when:
        def expression = diceFactory.parseExpression("3d6")
        def result = expression.evaluate(random)

        then:
        expression instanceof Dice
        ((Dice) expression).count == 3
        ((Dice) expression).sides == 6
        result.part == expression
        result.total == 6
        result.values.size() == 3
        result.values.stream().allMatch { it == 2 }
        result.results.isEmpty()
    }

    def "parseExpression should correctly parse valid constant expression"() {
        when:
        def expression = diceFactory.parseExpression("10")
        def result = expression.evaluate(random)

        then:
        expression instanceof Constant
        ((Constant) expression).value == 10
        result.part == expression
        result.total == 10
        result.results.isEmpty()
        result.values.size() == 1
        result.values.first == 10
    }

    def "parseExpression should throw IllegalArgumentException for invalid dice expression missing sides"() {
        when:
        diceFactory.parseExpression("3d")

        then:
        thrown(IllegalArgumentException)
    }

    def "parseExpression should throw IllegalArgumentException for invalid dice expression missing count"() {
        when:
        diceFactory.parseExpression("d6")

        then:
        thrown(IllegalArgumentException)
    }

    def "parseExpression should throw IllegalArgumentException for invalid dice expression"() {
        when:
        diceFactory.parseExpression("adc")

        then:
        thrown(IllegalArgumentException)
    }

    def "parseExpression should throw IllegalArgumentException for invalid constant expression"() {
        when:
        diceFactory.parseExpression("abc")

        then:
        thrown(IllegalArgumentException)
    }

    def "parseExpression should correctly parse composite expression with addition operator"() {
        when:
        def result = diceFactory.parseExpression("4d6+5")

        then:
        result instanceof DiceExpressionOperation
        ((DiceExpressionOperation) result).operator == DiceExpressionOperator.ADD
        ((DiceExpressionOperation) result).parts.first instanceof Dice
        ((DiceExpressionOperation) result).parts.last instanceof Constant
        result.evaluate(random).total == (8 + 5)
    }

    def "parseExpression should correctly parse composite expression with subtraction operator"() {
        when:
        def result = diceFactory.parseExpression("6-2d5")

        then:
        result instanceof DiceExpressionOperation
        ((DiceExpressionOperation) result).operator == DiceExpressionOperator.SUBTRACT
        ((DiceExpressionOperation) result).parts.first instanceof Constant
        ((DiceExpressionOperation) result).parts.last instanceof Dice
        result.evaluate(random).total == (6 - 4)
    }

    def "parseExpression should throw IllegalArgumentException for invalid composite expression missing right operand"() {
        when:
        diceFactory.parseExpression("3d6-")

        then:
        thrown(IllegalArgumentException)
    }

    def "parseExpression should throw IllegalArgumentException for invalid composite expression missing left operand"() {
        when:
        diceFactory.parseExpression("+5")

        then:
        thrown(IllegalArgumentException)
    }

    def "parseExpression should throw IllegalArgumentException for more than one operator"() {
        when:
        diceFactory.parseExpression("1+2d6-3")

        then:
        thrown(IllegalArgumentException)
    }
}
