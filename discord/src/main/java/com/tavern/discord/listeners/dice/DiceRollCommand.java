package com.tavern.discord.listeners.dice;

import com.tavern.discord.layer.command.slash.annotations.*;

@SlashCommand(name = "dice", description = "Roll a dice. Default: 1d6")
public class DiceRollCommand {
    private final int count;
    private final int sides;

    @SlashCommandCreator
    public DiceRollCommand(
        @SlashCommandOption(name = "count", description = "The number of dice to roll") int count,
        @SlashCommandOption(name = "sides", description = "The number of sides on each dice") int sides
    ) {
        this.count = count;
        this.sides = sides;
    }

    @Override
    public String toString() {
        return "DiceRollCommand{" +
            "count='" + count + '\'' +
            ", sides='" + sides + '\'' +
            '}';
    }

    public int getCount() {
        return count;
    }

    public int getSides() {
        return sides;
    }
}
