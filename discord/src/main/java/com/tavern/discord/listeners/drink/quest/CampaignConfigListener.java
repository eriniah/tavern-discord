package com.tavern.discord.listeners.drink.quest;

import com.tavern.discord.layer.annotations.Inject;
import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandHandler;
import com.tavern.domain.model.discord.guild.config.GuildConfigRepository;

public class CampaignConfigListener implements SlashCommandListener {

    @Inject
    public GuildConfigRepository guildConfigRepository;

    @Override
    public TavernSlashCommand getCommand(TavernSlashCommandFactory factory) {
        return factory.builder("campaign", "Drinking campaign")
            .addSubCommand(CampaignConfigCommand.class)
            .addSimpleSubCommand("start", "Start a new campaign")
            .addSimpleSubCommand("stop", "Stop the current campaign")
            .build();
    }

    @SlashCommandHandler(CampaignConfigCommand.class)
    public String config(CampaignConfigCommand command) {
        return "config " + command.toString();
    }

    @SlashCommandHandler(command = "campaign", subCommand = "start")
    public String start() {
        return "Starting a new campaign...";
    }

    @SlashCommandHandler(command = "campaign", subCommand = "stop")
    public String stop() {
        return "Stopping the current campaign...";
    }
}
