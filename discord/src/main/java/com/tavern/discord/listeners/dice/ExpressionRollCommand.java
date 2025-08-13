package com.tavern.discord.listeners.dice;

import com.tavern.discord.layer.command.slash.annotations.*;

@SlashCommand(name = "expression", description = "Custom roll expression similar to roll20")
public class ExpressionRollCommand {
    private final String expression;
    private final boolean hidden;

    @SlashCommandCreator
    public ExpressionRollCommand(
        @SlashCommandOption(
            name = "expression",
            description = "Format 'NdX [[{+|-} {NdX|M}]...]' where: N = number of dice, X = sides of the dice and M = integer",
            required = true
        ) String expression,
        @SlashCommandOption(
            name = "hidden",
            description = "Hide this dice roll from other users"
        ) Boolean hidden
    ) {
        this.expression = expression;
        this.hidden = null != hidden && hidden;
    }

    @Override
    public String toString() {
        return "ExpressionRollCommand{" +
            "expression='" + expression + '\'' +
            ", hidden=" + hidden +
            '}';
    }

    public String getExpression() {
        return expression;
    }

    public boolean isHidden() {
        return hidden;
    }
}
