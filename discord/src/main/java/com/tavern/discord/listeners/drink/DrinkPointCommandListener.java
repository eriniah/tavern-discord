package com.tavern.discord.listeners.drink;

import com.tavern.discord.layer.annotations.Context;
import com.tavern.discord.layer.annotations.Inject;
import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class DrinkPointCommandListener implements SlashCommandListener {

    @Inject
    private DrinkPointCache cache;

    @Context
    private SlashCommandInteractionEvent event;

    @Override
    public TavernSlashCommand getCommand(TavernSlashCommandFactory factory) {
        return factory.builder("point", "Meaningless points")
            .addSubCommand(AddDrinkPointCommand.class)
            .addSubCommand(GetDrinkPointCommand.class)
            .build();
    }

    @SlashCommandHandler()
    public void handle(AddDrinkPointCommand command) {
        String userId = event.getUser().getId();
        cache.add(userId, command.getCount());

        event.reply("Added %d points. You are now at %d points.".formatted(
            command.getCount(),
            cache.get(userId)
        )).queue();
    }

    @SlashCommandHandler
    public void handle(GetDrinkPointCommand command) {
        StringBuilder sb = new StringBuilder()
            .append("Drink points:\n");
        cache.getPoints().forEach((userId, points) ->
            sb.append(event.getJDA().getUserById(userId).getName())
                .append(": ")
                .append(points)
                .append("\n")
        );

        event.reply(sb.toString()).queue();
    }
}
