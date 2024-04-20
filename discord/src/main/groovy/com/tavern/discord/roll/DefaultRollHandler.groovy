package com.tavern.discord.roll


import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import com.tavern.domain.model.roll.RollService
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.ext.XLogger
import org.slf4j.ext.XLoggerFactory

import javax.annotation.Nonnull

class DefaultRollHandler implements CommandHandler {
	private static final XLogger logger = XLoggerFactory.getXLogger(DefaultRollHandler.class)

	private final RollService rollService

	DefaultRollHandler(RollService rollService) {
		this.rollService = rollService
	}

	@Override
	Command getCommand() {
		TavernCommands.ROLL
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		TavernCommands.RollUsages.DEFAULT == usage
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		int roll = rollService.rollSingle(6)
		event.getChannel().sendMessage("You rolled a " + roll).queue({-> logger.trace('Sent default roll result')}, {-> logger.error('Failed to send default roll result')})
		return new CommandResultBuilder().success().build()
	}

}
