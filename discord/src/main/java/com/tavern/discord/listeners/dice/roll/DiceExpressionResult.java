package com.tavern.discord.listeners.dice.roll;

import com.tavern.utilities.CollectionUtils;

import java.util.*;
import java.util.function.IntBinaryOperator;

public final class DiceExpressionResult {
    private final DiceExpressionPart part;
    private final int total;
    private final List<DiceExpressionResult> results;
    private final List<Integer> values;

    private DiceExpressionResult(DiceExpressionPart part, int total, List<DiceExpressionResult> results, List<Integer> values) {
        this.part = part;
        this.total = total;
        this.results = CollectionUtils.wrapIfPresent(results, ArrayList::new);
        this.values = CollectionUtils.wrapIfPresent(values, ArrayList::new);
    }

    public DiceExpressionPart getPart() {
        return part;
    }

    public int getTotal() {
        return total;
    }

    public List<DiceExpressionResult> getResults() {
        return new ArrayList<>(results);
    }

    public List<Integer> getValues() {
        return new ArrayList<>(values);
    }

    static ValuesBuilder values(DiceExpressionPart part) {
        return new ValuesBuilder(part);
    }

    static final class ValuesBuilder {
        private final DiceExpressionPart part;
        private final List<Integer> values;

        ValuesBuilder(DiceExpressionPart part) {
            this.part = part;
            this.values = new ArrayList<>();
        }

        ValuesBuilder add(int value) {
            values.add(value);
            return this;
        }

        DiceExpressionResult build() {
            return new DiceExpressionResult(
                part,
                values.stream().mapToInt(v -> v).sum(),
                Collections.emptyList(),
                values
            );
        }
    }

    static CompositeBuilder composite(DiceExpressionPart part) {
        return new CompositeBuilder(part);
    }

    static final class CompositeBuilder {
        private final DiceExpressionPart part;
        private final List<DiceExpressionResult> results;

        CompositeBuilder(DiceExpressionPart part) {
            this.part = part;
            this.results = new ArrayList<>();
        }

        CompositeBuilder add(DiceExpressionResult result) {
            results.add(result);
            return this;
        }

        DiceExpressionResult build(IntBinaryOperator operation) {
            return new DiceExpressionResult(
                part,
                results.stream().mapToInt(DiceExpressionResult::getTotal).reduce(operation).orElse(0),
                results,
                Collections.emptyList()
            );
        }
    }
}
