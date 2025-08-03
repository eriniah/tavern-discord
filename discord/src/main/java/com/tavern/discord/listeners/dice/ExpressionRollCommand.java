package com.tavern.discord.listeners.dice;

import com.tavern.discord.layer.command.slash.annotations.*;

@SlashCommand(name = "expression", description = "Custom roll expression similar to roll20")
public class ExpressionRollCommand {
    private final String expression;

    @SlashCommandCreator
    public ExpressionRollCommand(
        @SlashCommandOption(
            name = "expression",
            description = "Format 'NdX [[{+|-} {NdX|M}]...]' where: N = number of dice, X = sides of the dice and M = integer",
            required = true
        ) String expression
    ) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "ExpressionRollCommand{" +
            "expression='" + expression + '\'' +
            '}';
    }

    public String getExpression() {
        return expression;
    }
}
