package com.tavern.discord.listeners.dice;

import com.tavern.discord.layer.command.slash.annotations.*;
import jakarta.annotation.Nullable;

@SlashCommand(name = "dice", description = "Roll a dice. Default: 1d6")
public class DiceRollCommand {
    private final int count;
    private final int sides;
    private final boolean hidden;

    @SlashCommandCreator
    public DiceRollCommand(
        @SlashCommandOption(name = "count", description = "The number of dice to roll") int count,
        @SlashCommandOption(name = "sides", description = "The number of sides on each dice") int sides,
        @SlashCommandOption(
            name = "hidden",
            description = "Hide this dice roll from other users"
        ) @Nullable Boolean hidden
    ) {
        this.count = count;
        this.sides = sides;
        this.hidden = null != hidden && hidden;
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

    public boolean isHidden() {
        return hidden;
    }
}
