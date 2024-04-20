package com.tavern.discord.audio

import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.audio.SongId
import com.tavern.domain.model.audio.SongService
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull

class RemoveSongCommandHandler implements CommandHandler {
	private final SongService songService

	RemoveSongCommandHandler(SongService songService) {
		this.songService = songService
	}

	@Override
	Command getCommand() {
		TavernCommands.SONGS
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		TavernCommands.SongsUsages.REMOVE == usage
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		String id = message.args[1]
		songService.remove(new SongId(id))
		event.getChannel().sendMessage("Removed song ${id}").queue()
		new CommandResultBuilder().success().build()
	}
}
