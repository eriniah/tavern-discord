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

class AmountAndSidesRollHandler implements CommandHandler {
	private static final XLogger logger = XLoggerFactory.getXLogger(AmountAndSidesRollHandler.class)

	private final RollService rollService

	AmountAndSidesRollHandler(RollService rollService) {
		this.rollService = rollService
	}

	@Override
	Command getCommand() {
		TavernCommands.ROLL
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		TavernCommands.RollUsages.AMOUNT_AND_SIDES == usage
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		try {
			int amount = Integer.parseInt(message.args.first())
			int sides = Integer.parseInt(message.args.last())

			List<Integer> rolls = rollService.roll(amount, sides)
			StringJoiner joiner = new StringJoiner('+')
			rolls.forEach({roll -> joiner.add(" ${roll} ")})
			String result = "You rolled: ${joiner.toString()} = ${rolls.stream().reduce(0, Integer::sum)}"
			event.getChannel().sendMessage(result).queue({-> logger.trace('Sent default roll result')}, {-> logger.error('Failed to send default roll result')})
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
