package com.tavern.discord.drinks


import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import com.tavern.domain.model.discord.Mention
import com.tavern.domain.model.drinks.DrinkService
import com.tavern.domain.model.drinks.MemberDrinkResult
import com.tavern.discord.DiscordUtils
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull
import java.util.stream.Collectors

class PopPopCommandHandler implements CommandHandler {
	private final DrinkService drinkService

	PopPopCommandHandler(DrinkService drinkService) {
		this.drinkService = drinkService
	}

	@Override
	Command getCommand() {
		TavernCommands.POPPOP
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		DiscordUtils.getUsersVoiceChannel(event.getJDA(), event.getAuthor().id)
				.map(channel -> {
					List<MemberDrinkResult> results = drinkService.popPop(channel.getMembers())
					if (results[0].shot) {
						event.getChannel().sendMessage("Pop Pop!\nSucks to suck. Everyone take a shot!").queue()
					} else {
						event.getChannel().sendMessage("Pop Pop!\n" + String.join("\n", results.stream().map(result -> "${new Mention(result.member.id)} take ${result.drinks} drinks!").collect(Collectors.toList()))).queue()
					}
					new CommandResultBuilder().success().build()
				}).orElse(_ -> {
					event.getChannel().sendMessage("Must be in a voice chat to pop pop")
					new CommandResultBuilder().error().build()
				})
	}

}
