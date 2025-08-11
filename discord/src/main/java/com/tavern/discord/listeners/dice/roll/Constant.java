package com.tavern.discord.listeners.dice.roll;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

record Constant(int value) implements DiceExpressionValue {

    Constant {
        if (value < 0 || UPPER_BOUND < value) {
            throw new IllegalArgumentException("Constant must be greater than or equal to 0 and less than " + UPPER_BOUND);
        }
    }

    @Override
    public DiceExpression roll(Random random) {
        return this;
    }

    @Override
    public int evaluate(Random random) {
        return value;
    }

    @Override
    public String getRepresentation() {
        return "" + value;
    }

    @Override
    public String toString() {
        return getRepresentation();
    }
}
