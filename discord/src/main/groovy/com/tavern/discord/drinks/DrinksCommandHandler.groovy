package com.tavern.discord.drinks


import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import com.tavern.domain.model.discord.Mention
import com.tavern.domain.model.discord.UserId
import com.tavern.domain.model.drinks.DrinkService
import com.tavern.discord.DiscordUtils
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull
import java.util.stream.Collectors

class DrinksCommandHandler implements CommandHandler {
	private final DrinkService drinkService

	DrinksCommandHandler(DrinkService drinkService) {
		this.drinkService = drinkService
	}

	@Override
	Command getCommand() {
		TavernCommands.DRINKS
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		List<Member> members = DiscordUtils.getUsersVoiceChannel(event.getJDA(), event.getMember().id)
				.map(channel -> channel.members)
				.orElse([event.getMember()])

		event.getChannel().sendMessage("Drink totals\n" + String.join("\n", members.stream().map(member -> "${new Mention(member)}: ${drinkService.getDrinkRepository().getDrinks(new UserId(member.id))}").collect(Collectors.toList()))).queue()
		new CommandResultBuilder().success().build()
	}

}
