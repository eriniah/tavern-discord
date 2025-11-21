package com.tavern.discord.listeners.drink;

import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandHandler;

public class PopPopCommandListener implements SlashCommandListener {

    @Override
    public TavernSlashCommand getCommand(TavernSlashCommandFactory factory) {
        return factory.simpleNoOptions("poppop", "Pop pop!");
    }

    @SlashCommandHandler(command = "poppop")
    public String handle() {
        return "@193474871190093825 #1195481255756701838 Pop pop!";
    }

}
