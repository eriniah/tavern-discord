package com.tavern.discord.listeners.dice.roll;

import java.util.List;

public interface DiceExpressionOperation extends DiceExpressionPart {
    List<DiceExpressionPart> getParts();
    DiceExpressionOperator getOperator();

}
