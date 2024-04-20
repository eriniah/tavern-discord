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

class StopCommandHandler implements CommandHandler {
	private final AudioService audioService

	StopCommandHandler(AudioService audioService) {
		this.audioService = audioService
	}

	@Override
	Command getCommand() {
		TavernCommands.STOP
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		audioService.stop(new GuildId(event.getGuild().getId()))
		new CommandResultBuilder().success().build()
	}

}
