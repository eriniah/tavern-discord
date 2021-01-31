package com.asm.tavern.discord.discord.roll

import com.asm.tavern.domain.model.DomainRegistry
import com.asm.tavern.domain.model.TavernCommands
import com.asm.tavern.domain.model.command.Command
import com.asm.tavern.domain.model.command.CommandArgumentUsage
import com.asm.tavern.domain.model.command.CommandHandler
import com.asm.tavern.domain.model.command.CommandMessage
import com.asm.tavern.domain.model.command.CommandResult
import com.asm.tavern.domain.model.command.CommandResultBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.slf4j.ext.XLogger
import org.slf4j.ext.XLoggerFactory

import javax.annotation.Nonnull

class AmountXSidesHandler implements CommandHandler {
	private static final XLogger logger = XLoggerFactory.getXLogger(AmountXSidesHandler.class);

	@Override
	Command getCommand() {
		TavernCommands.ROLL
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		TavernCommands.RollUsages.AMOUNT_X_SIDES == usage
	}

	@Override
	CommandResult handle(@Nonnull GuildMessageReceivedEvent event, CommandMessage message) {
		String[] amountAndSides = message.args[0].toLowerCase().split('x')
		int amount = Integer.parseInt(amountAndSides[0])
		int sides = Integer.parseInt(amountAndSides[1])

		List<Integer> rolls = DomainRegistry.rollService().roll(amount, sides)
		StringJoiner joiner = new StringJoiner('+')
		rolls.forEach({roll -> joiner.add(" ${roll} ")})
		String result = "You rolled: ${joiner.toString()} = ${rolls.stream().reduce(0, Integer::sum)}"
		event.getChannel().sendMessage(result).queue({-> logger.trace('Sent default roll result')}, {-> logger.error('Failed to send default roll result')})
		return new CommandResultBuilder().success().build()
	}

}
