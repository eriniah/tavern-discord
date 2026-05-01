package com.tavern.discord.listeners.drink.quest;

import com.tavern.discord.layer.command.slash.annotations.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;


@SlashCommand(name = "config", description = "Configure drinking campaigns")
public record CampaignConfigCommand(TextChannel channel) {

    @SlashCommandCreator
    public CampaignConfigCommand(
        @SlashCommandOption(name = "channel", description = "The text channel to send campaign messages to") TextChannel channel
    ) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "CampaignConfigCommand{" +
            "channel=" + channel +
            '}';
    }
}
