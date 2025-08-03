package com.tavern.discord.listeners.dice.roll;

import java.util.List;

final class DiceExpressionOperationAdd extends AbstractDiceExpressionOperation {

    DiceExpressionOperationAdd(List<DiceExpressionPart> parts) {
        super(parts, Integer::sum);
    }

    @Override
    public DiceExpressionOperator getOperator() {
        return DiceExpressionOperator.ADD;
    }

}
