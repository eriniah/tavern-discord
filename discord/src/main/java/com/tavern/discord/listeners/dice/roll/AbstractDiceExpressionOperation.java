package com.tavern.discord.listeners.dice.roll;

import com.tavern.utilities.CollectionUtils;

import java.util.*;
import java.util.function.IntBinaryOperator;

abstract class AbstractDiceExpressionOperation implements DiceExpressionOperation {
    private final List<DiceExpressionPart> parts;
    private final IntBinaryOperator operation;

    protected AbstractDiceExpressionOperation(List<DiceExpressionPart> parts, IntBinaryOperator operation) {
        this.parts = CollectionUtils.wrapIfPresent(parts, ArrayList::new);
        this.operation = operation;
    }

    @Override
    public List<DiceExpressionPart> getParts() {
        return new ArrayList<>(parts);
    }

    @Override
    public DiceExpressionResult evaluate(Random random) {
        DiceExpressionResult.CompositeBuilder result = DiceExpressionResult.composite(this);

        parts.stream().map(part -> part.evaluate(random))
            .forEach(result::add);

        return result.build(operation);
    }

    @Override
    public void visit(DiceExpressionVisitor visitor) {
        parts.getFirst().visit(visitor);
        for (int i = 1; i < parts.size(); i++) {
            visitor.visit(getOperator());
            parts.get(i).visit(visitor);
        }
    }

}
