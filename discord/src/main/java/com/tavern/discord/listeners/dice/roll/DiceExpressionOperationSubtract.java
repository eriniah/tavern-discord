package com.tavern.discord.listeners.dice.roll;

import java.util.List;

final class DiceExpressionOperationSubtract extends AbstractDiceExpressionOperation {

    DiceExpressionOperationSubtract(List<DiceExpressionPart> parts) {
        super(parts, (a, b) -> a - b);
    }

    @Override
    public DiceExpressionOperator getOperator() {
        return DiceExpressionOperator.SUBTRACT;
    }

}
