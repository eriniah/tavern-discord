package com.tavern.discord.help

import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import com.tavern.domain.model.help.CommandIniPrinter
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull

class HelpHandler implements CommandHandler {
	private String prefix

	HelpHandler(String prefix) {
		this.prefix = prefix
	}

	@Override
	Command getCommand() {
		TavernCommands.HELP
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		TavernCommands.HelpUsages.DEFAULT == usage
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		event.getChannel().sendMessage(new CommandIniPrinter().print(prefix, TavernCommands.getCommands())).queue()
		return new CommandResultBuilder().success().build()
	}

}
