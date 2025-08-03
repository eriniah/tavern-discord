package com.tavern.discord.listeners.dice.roll;

public interface DiceExpressionVisitor {
    void visit(DiceExpressionOperation operation);
    void visit(DiceExpressionOperator operator);
    void visit(DiceExpressionValue value);
}
