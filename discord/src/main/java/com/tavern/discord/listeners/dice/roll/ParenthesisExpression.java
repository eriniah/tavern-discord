package com.tavern.discord.listeners.dice.roll;

import jakarta.annotation.Nonnull;

import java.util.*;

final class ParenthesisExpression implements DiceExpression {
    private final DiceExpression part;

    ParenthesisExpression(@Nonnull DiceExpression part) {
        this.part = part;
    }

    @Override
    public DiceExpression roll(Random random) {
        return new ParenthesisExpression(part.roll(random));
    }

    @Override
    public int evaluate(Random random) {
        return part.evaluate(random);
    }

    @Override
    public String getRepresentation() {
        return String.format("(%s)", part.getRepresentation());
    }

}
