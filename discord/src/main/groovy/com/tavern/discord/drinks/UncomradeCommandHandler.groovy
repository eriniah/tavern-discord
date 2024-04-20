package com.tavern.discord.drinks

import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import com.tavern.domain.model.drinks.ComradeService
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull

class UncomradeCommandHandler implements CommandHandler {
	private final ComradeService comradeService

	UncomradeCommandHandler(ComradeService comradeService) {
		this.comradeService = comradeService
	}

	@Override
	Command getCommand() {
		TavernCommands.UNCOMRADE
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		comradeService.disable()
		event.getGuild().getSelfMember().modifyNickname(null).submit()
		new CommandResultBuilder().success().build()
	}
}
