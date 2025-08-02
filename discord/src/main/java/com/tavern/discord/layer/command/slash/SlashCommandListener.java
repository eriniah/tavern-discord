package com.tavern.discord.layer.command.slash;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface SlashCommandListener {

    SlashCommandData getCommand(TavernSlashCommandFactory builder);

}
