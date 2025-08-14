package com.tavern.discord.listeners.drink;

import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandHandler;

import java.time.Instant;
import java.util.Random;

public class DrinkCommandListener implements SlashCommandListener {
    private static final Random RANDOM = new Random(Instant.now().toEpochMilli());

    @Override
    public TavernSlashCommand getCommand(TavernSlashCommandFactory factory) {
        return factory.simpleNoOptions("drink", "Take a drink");
    }

    @SlashCommandHandler(command = "drink")
    public String handle() {
        int drinks = RANDOM.nextInt(5) + 1;
        return "Take %d drinks!".formatted(drinks);
    }
}
