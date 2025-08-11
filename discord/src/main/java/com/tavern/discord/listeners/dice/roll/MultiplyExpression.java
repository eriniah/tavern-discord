package com.tavern.discord.listeners.dice.roll;

import com.tavern.utilities.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

final class MultiplyExpression implements DiceExpression {
    private static final char OP_MULTIPLY = '*';

    private final List<DiceExpression> parts;

    private MultiplyExpression(List<DiceExpression> parts) {
        this.parts = CollectionUtils.wrapIfPresent(parts, ArrayList::new);
    }

    @Override
    public DiceExpression roll(Random random) {
        return new MultiplyExpression(
            parts.stream()
                .map(part -> part.roll(random))
                .collect(Collectors.toList())
        );
    }

    @Override
    public int evaluate(Random random) {
        if (parts.isEmpty()) {
            return 0;
        }

        int total = parts.getFirst().evaluate(random);
        if (1 < parts.size()) {
            for (int i = 1; i < parts.size(); i++) {
                total *= parts.get(i).evaluate(random);
            }
        }
        return total;
    }

    @Override
    public String getRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            DiceExpression part = parts.get(i);
            if (i == 0) {
                // No operator before first-part
                sb.append(part);
            } else {
                sb.append(" * ");
                sb.append(part.getRepresentation());
            }
        }
        return sb.toString();
    }

    static Builder builder() {
        return new Builder();
    }

    static final class Builder {
        private final List<DiceExpression> parts = new ArrayList<>();

        Builder multiply(DiceExpression expression) {
            parts.add(expression);
            return this;
        }

        MultiplyExpression build() {
            return new MultiplyExpression(parts);
        }
    }

}
