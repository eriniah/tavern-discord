package com.tavern.discord.listeners.dice.roll;

import java.util.Random;

final class Constant implements DiceExpressionValue {
    private final int value;

    Constant(int value) {
        if (value < 0 || UPPER_BOUND < value) {
            throw new IllegalArgumentException("Constant must be greater than or equal to 0 and less than " + UPPER_BOUND);
        }
        this.value = value;
    }

    @Override
    public DiceExpressionResult evaluate(Random random) {
        return DiceExpressionResult.values(this)
            .add(value)
            .build();
    }

    @Override
    public void visit(DiceExpressionVisitor visitor) {
        visitor.visit(this);
    }

    public int getValue() {
        return value;
    }
}
