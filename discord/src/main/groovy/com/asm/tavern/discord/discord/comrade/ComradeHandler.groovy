package com.asm.tavern.discord.discord.comrade

import com.asm.tavern.domain.model.TavernCommands
import com.asm.tavern.domain.model.command.Command
import com.asm.tavern.domain.model.command.CommandArgumentUsage
import com.asm.tavern.domain.model.command.CommandHandler
import com.asm.tavern.domain.model.command.CommandMessage
import com.asm.tavern.domain.model.command.CommandResult
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

import javax.annotation.Nonnull

class ComradeHandler implements CommandHandler {

	@Override
	Command getCommand() {
		TavernCommands.COMRADE
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		new HashSet<>(TavernCommands.COMRADE.argumentUsages).contains(usage)
	}

	@Override
	CommandResult handle(@Nonnull GuildMessageReceivedEvent event, CommandMessage message) {

	}

}
