package com.tavern.discord.listeners.dice.roll;

import com.tavern.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

final class AddAndSubtractExpression implements DiceExpression {
    private static final char OP_ADD = '+';
    private static final char OP_SUBTRACT = '-';

    private final List<AddPart> parts;

    private AddAndSubtractExpression(List<AddPart> parts) {
        this.parts = CollectionUtils.wrapIfPresent(parts, ArrayList::new);
    }

    @Override
    public DiceExpression roll(Random random) {
        return new AddAndSubtractExpression(
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

        int total = parts.getFirst().expression().evaluate(random);
        if (1 < parts.size()) {
            for (int i = 1; i < parts.size(); i++) {
                AddPart part = parts.get(i);
                if (OP_ADD == part.operator()) {
                    total += part.expression().evaluate(random);
                } else if (OP_SUBTRACT == part.operator()) {
                    total -= part.expression().evaluate(random);
                } else {
                    throw new UnsupportedOperationException("Unknown operator: " + part.operator());
                }
            }
        }
        return total;
    }

    @Override
    public String getRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            AddPart part = parts.get(i);
            if (i == 0) {
                // No operator before first-part
                sb.append(part.expression().getRepresentation());
            } else {
                sb.append(" ").append(part.getRepresentation());
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getRepresentation();
    }

    static AddAndSubtractExpression add(List<DiceExpression> expressions) {
        Builder builder = builder();
        for (DiceExpression expression : expressions) {
            builder.add(expression);
        }
        return builder.build();
    }

    static Builder builder() {
        return new Builder();
    }

    static final class Builder {
        private final List<AddPart> parts = new ArrayList<>();

        Builder add(DiceExpression expression) {
            parts.add(new AddPart(OP_ADD, expression));
            return this;
        }

        Builder subtract(DiceExpression expression) {
            parts.add(new AddPart(OP_SUBTRACT, expression));
            return this;
        }

        AddAndSubtractExpression build() {
            return new AddAndSubtractExpression(parts);
        }
    }

    private record AddPart(char operator, DiceExpression expression) {
        String getRepresentation() {
            return String.format("%s %s", operator, expression.getRepresentation());
        }

        AddPart roll(Random random) {
            return new AddPart(operator, expression.roll(random));
        }
    }

}
