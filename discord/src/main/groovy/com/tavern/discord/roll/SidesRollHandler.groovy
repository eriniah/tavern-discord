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

import javax.annotation.Nonnull

class SidesRollHandler implements CommandHandler {
	private final RollService rollService

	SidesRollHandler(RollService rollService) {
		this.rollService = rollService
	}

	@Override
	Command getCommand() {
		TavernCommands.ROLL
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		TavernCommands.RollUsages.SIDES == usage
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		try {
			int sides = Integer.parseInt(message.args.first())
			int roll = rollService.rollSingle(sides)
			event.getChannel().sendMessage("You rolled a " + roll).queue()
			return new CommandResultBuilder().success().build()
		} catch (NumberFormatException ex) {
			event.getChannel().sendMessage("Arguments must be numbers")
			return new CommandResultBuilder().error().build()
		} catch (IllegalArgumentException ex) {
			event.getChannel().sendMessage(ex.getMessage())
			return new CommandResultBuilder().error().build()
		}
	}
}
