package com.tavern.discord.listeners.dice.roll;

import java.util.List;
import java.util.function.Function;

public enum DiceExpressionOperator {
    ADD("+", DiceExpressionOperationAdd::new),
    SUBTRACT("-", DiceExpressionOperationSubtract::new);

    private final String operator;
    private final Function<List<DiceExpressionPart>, DiceExpressionOperation> operationFactory;

    DiceExpressionOperator(String operator, Function<List<DiceExpressionPart>, DiceExpressionOperation> operationFactory) {
        this.operator = operator;
        this.operationFactory = operationFactory;
    }

    public String getOperator() {
        return operator;
    }

    public DiceExpressionOperation createOperation(List<DiceExpressionPart> parts) {
        return operationFactory.apply(parts);
    }

    public static DiceExpressionOperator get(String operator) {
        for (DiceExpressionOperator op : values()) {
            if (op.getOperator().equals(operator)) {
                return op;
            }
        }
        throw new IllegalArgumentException("No operator with symbol " + operator);
    }

    public static List<DiceExpressionOperator> orderedOperators() {
        return List.of(ADD, SUBTRACT);
    }

}
