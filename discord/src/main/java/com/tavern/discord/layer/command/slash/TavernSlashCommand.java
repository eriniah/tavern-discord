package com.tavern.discord.layer.command.slash;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface TavernSlashCommand {
    SlashCommandData getSlashCommand();
}
