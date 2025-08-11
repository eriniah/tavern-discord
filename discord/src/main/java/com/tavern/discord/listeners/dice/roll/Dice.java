package com.tavern.discord.listeners.dice.roll;

import java.util.*;
import java.util.stream.Collectors;

record Dice(int count, int sides) implements DiceExpressionValue {

    Dice {
        if (count < 1 || UPPER_BOUND < count) {
            throw new IllegalArgumentException("Count must be greater than 0 and less than " + UPPER_BOUND);
        }
        if (sides < 1 || UPPER_BOUND < sides) {
            throw new IllegalArgumentException("Sides must be greater than 0 and less than" + UPPER_BOUND);
        }
    }

    @Override
    public DiceExpression roll(Random random) {
        return new ParenthesisExpression(
            AddAndSubtractExpression.add(
                doRoll(random).stream()
                    .map(Constant::new)
                    .collect(Collectors.toList())
            )
        );
    }

    private List<Integer> doRoll(Random random) {
        List<Integer> rolls = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            rolls.add(random.nextInt(sides) + 1);
        }

        return rolls;
    }

    @Override
    public int evaluate(Random random) {
        return doRoll(random).stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String getRepresentation() {
        return String.format("%dd%d", count, sides);
    }
}
