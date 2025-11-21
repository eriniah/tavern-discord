package com.tavern.discord.listeners.drink;

import com.tavern.discord.layer.command.slash.annotations.*;

import java.util.Objects;

@SlashCommand(name = "add", description = "Add meaningless points")
public class AddDrinkPointCommand {
    private final int count;

    @SlashCommandCreator
    public AddDrinkPointCommand(
        @SlashCommandOption(name = "count", required = false, description = "The number of points to add") Integer count
    ) {
        this.count = Objects.requireNonNullElse(count, 1);
    }

    public int getCount() {
        return this.count;
    }

}
