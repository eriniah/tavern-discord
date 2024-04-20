package com.tavern.discord.roll

import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull

class RollTideHandler implements CommandHandler {

	@Override
	Command getCommand() {
		TavernCommands.ROLL
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		TavernCommands.RollUsages.ROLL_TIDE == usage
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		event.getChannel().sendMessage('What are you doing step bro!?').queue()
		return new CommandResultBuilder().success().build()
	}

}
