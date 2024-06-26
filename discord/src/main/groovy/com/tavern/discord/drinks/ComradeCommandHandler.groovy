package com.tavern.discord.drinks

import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.audio.AudioService
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import com.tavern.domain.model.drinks.ComradeService
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull
import java.time.OffsetDateTime

class ComradeCommandHandler implements CommandHandler {
	private final ComradeService comradeService
	private final AudioService audioService

	ComradeCommandHandler(ComradeService comradeService, AudioService audioService) {
		this.comradeService = comradeService
		this.audioService = audioService
	}

	@Override
	Command getCommand() {
		TavernCommands.COMRADE
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		comradeService.enable()
		MessageEmbed embeddedImage = new MessageEmbed(
				"https://media3.giphy.com/media/befaYZCgtZfZm/giphy.gif",
				"Comrades",
				"Comrades",
				EmbedType.IMAGE,
				OffsetDateTime.now(),
				0xFF0000,
				null,
				new MessageEmbed.Provider("Giphy", "https://giphy.com/"),
				null,
				null,
				null,
				new MessageEmbed.ImageInfo("https://media3.giphy.com/media/befaYZCgtZfZm/giphy.gif", "https://media3.giphy.com/media/befaYZCgtZfZm/giphy.gif", 400, 300),
				null)
		event.getChannel().asTextChannel().sendMessageEmbeds(embeddedImage).queue()
		event.getGuild().getSelfMember().modifyNickname("Comrade Bot").submit()
		audioService.join(event.getMember().getVoiceState(), event.getGuild().getAudioManager())
		audioService.play(event.getChannel().asTextChannel(), URI.create("https://www.youtube.com/watch?v=bCBJ-MaUTC4&ab_channel=ChornyyBaron"))
		new CommandResultBuilder().success().build()
	}

}
