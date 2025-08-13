package com.tavern.discord.listeners.dice.roll;

import java.util.*;
import java.util.stream.Collectors;

record RolledDice(int count, int sides, List<Integer> values) implements DiceExpressionValue {

    @Override
    public DiceExpression roll(Random random) {
        return this;
    }

    @Override
    public int evaluate(Random random) {
        return values.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String getRepresentation(DiceExpressionValueFormatter formatter) {
        return formatter.formatRoll(count, sides, values);
    }

    @Override
    public String toString() {
        return getRepresentation(DiceExpressionValueFormatter.getDefault());
    }
}
