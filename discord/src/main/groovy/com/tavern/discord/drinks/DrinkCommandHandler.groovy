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
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull
import java.util.stream.Collectors

class DrinkCommandHandler implements CommandHandler {
	private final DrinkService drinkService

	DrinkCommandHandler(DrinkService drinkService) {
		this.drinkService = drinkService
	}

	@Override
	Command getCommand() {
		TavernCommands.DRINK
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		List<Member> members = DiscordUtils.getUsersVoiceChannel(event.getJDA(), event.getAuthor().id)
				.map(channel -> channel.getMembers())
				.orElse([])
		List<MemberDrinkResult> results = drinkService.drink(event.getMember(), members)
		event.getChannel().sendMessage(String.join("\n", results.stream().map(result -> "${new Mention(result.member.id)} take ${result.drinks} drinks!").collect(Collectors.toList()))).queue()
		new CommandResultBuilder().success().build()
	}
}
