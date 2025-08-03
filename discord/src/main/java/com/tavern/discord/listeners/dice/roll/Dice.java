package com.tavern.discord.listeners.dice.roll;

import java.util.Random;
import java.util.stream.IntStream;

final class Dice implements DiceExpressionValue {
    private final int count;
    private final int sides;

    Dice(int count, int sides) throws IllegalArgumentException {
        if (count < 1 || UPPER_BOUND < count) {
            throw new IllegalArgumentException("Count must be greater than 0 and less than " + UPPER_BOUND);
        }
        this.count = count;

        if (sides < 1 || UPPER_BOUND < sides) {
            throw new IllegalArgumentException("Sides must be greater than 0 and less than" + UPPER_BOUND);
        }
        this.sides = sides;
    }

    @Override
    public DiceExpressionResult evaluate(Random random) {
        DiceExpressionResult.ValuesBuilder result = DiceExpressionResult.values(this);

        IntStream.range(0, count)
            .map(__ -> random.nextInt(1, sides + 1))
            .forEach(result::add);

        return result.build();
    }

    @Override
    public void visit(DiceExpressionVisitor visitor) {
        visitor.visit(this);
    }

    public int getCount() {
        return count;
    }

    public int getSides() {
        return sides;
    }
}
