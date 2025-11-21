package com.tavern.discord.listeners.drink;

import com.tavern.discord.layer.command.slash.annotations.SlashCommand;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandCreator;

@SlashCommand(name = "get", description = "Get your meaningless drink points")
public class GetDrinkPointCommand {

    @SlashCommandCreator
    public GetDrinkPointCommand() {
    }

}
