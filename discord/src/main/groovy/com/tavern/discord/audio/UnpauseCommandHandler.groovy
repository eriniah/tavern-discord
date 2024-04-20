package com.tavern.discord.audio


import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.audio.AudioService
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import com.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull

class UnpauseCommandHandler implements CommandHandler {
	private final AudioService audioService

	UnpauseCommandHandler(AudioService audioService) {
		this.audioService = audioService
	}

	@Override
	Command getCommand() {
		TavernCommands.UNPAUSE
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		audioService.unpause(new GuildId(event.getGuild().getId()))
		GuildId guildId = new GuildId(event.getGuild().getId())
		String title = audioService.getNowPlaying(guildId).info.title
		event.getChannel().sendMessage("Resuming: ${title}").queue()
		new CommandResultBuilder().success().build()
	}
}
