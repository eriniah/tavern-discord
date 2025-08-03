package com.tavern.discord.listeners.dice.roll;

import java.util.Random;

public interface DiceExpressionPart {
    DiceExpressionResult evaluate(Random random);
    void visit(DiceExpressionVisitor visitor);
}
