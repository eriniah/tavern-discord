package com.asm.tavern.discord.discord.audio


import com.asm.tavern.domain.model.TavernCommands
import com.asm.tavern.domain.model.audio.AudioService
import com.asm.tavern.domain.model.command.*
import com.asm.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

import javax.annotation.Nonnull

class PauseCommandHandler implements CommandHandler {
	private final AudioService audioService

	PauseCommandHandler(AudioService audioService) {
		this.audioService = audioService
	}

	@Override
	Command getCommand() {
		TavernCommands.PAUSE
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull GuildMessageReceivedEvent event, CommandMessage message) {
        GuildId guildId = new GuildId(event.getGuild().getId())
        if(!audioService.getIsPaused(guildId)){
            audioService.pause(guildId)
            String title = audioService.getNowPlaying(guildId).info.title
            event.getChannel().sendMessage("Pausing: ${title}").queue()
        }
        else if(audioService.getIsPaused(guildId)){
            audioService.unpause(guildId)
            String title = audioService.getNowPlaying(guildId).info.title
            event.getChannel().sendMessage("Resuming: ${title}").queue()
        }
		new CommandResultBuilder().success().build()
	}

}
