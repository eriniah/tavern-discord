package com.tavern.discord.listeners.dice.roll;

import java.util.Random;

public interface DiceExpression {
    /**
     * Rolls all dice but leaves all other expressions as-is.
     * Example: 2d4 + 2 -> (2 + 1) + 2
     * @param random Random number generator
     * @return The rolled expression
     */
    DiceExpression roll(Random random);
    /**
     * Evaluate the full expression
     * @param random Random number generator
     * @return The total value
     */
    int evaluate(Random random);
    /**
     * Get the expression as a string
     * @param formatter Formatter to use for formatting values
     * @return The string expression representation
     */
    String getRepresentation(DiceExpressionValueFormatter formatter);
}
