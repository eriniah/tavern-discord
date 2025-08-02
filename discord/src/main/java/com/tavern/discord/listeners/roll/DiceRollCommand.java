package com.tavern.discord.listeners.roll;

import com.tavern.discord.layer.command.slash.annotations.*;

@SlashCommand(name = "dice", description = "Roll a dice. Default: 1d6")
public class DiceRollCommand {
    private final String count;
    private final String sides;

    @SlashCommandCreator
    public DiceRollCommand(
        @SlashCommandOption(name = "count", description = "The number of dice to roll") String count,
        @SlashCommandOption(name = "sides", description = "The number of sides on each dice") String sides
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

    public String getCount() {
        return count;
    }

    public String getSides() {
        return sides;
    }
}
