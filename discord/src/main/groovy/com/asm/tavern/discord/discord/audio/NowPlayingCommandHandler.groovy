package com.asm.tavern.discord.discord.audio

import com.asm.tavern.domain.model.DomainRegistry
import com.asm.tavern.domain.model.TavernCommands
import com.asm.tavern.domain.model.audio.AudioTrackInfo
import com.asm.tavern.domain.model.command.Command
import com.asm.tavern.domain.model.command.CommandArgumentUsage
import com.asm.tavern.domain.model.command.CommandHandler
import com.asm.tavern.domain.model.command.CommandMessage
import com.asm.tavern.domain.model.command.CommandResult
import com.asm.tavern.domain.model.command.CommandResultBuilder
import com.asm.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

import javax.annotation.Nonnull

class NowPlayingCommandHandler implements CommandHandler {

	@Override
	Command getCommand() {
		TavernCommands.NOW_PLAYING
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull GuildMessageReceivedEvent event, CommandMessage message) {
		AudioTrackInfo trackInfo = DomainRegistry.audioService().getNowPlaying(new GuildId(event.getGuild().getId()))

		if (trackInfo) {
			event.getChannel().sendMessage(new MessageEmbed(
					trackInfo.url.toString(),
					trackInfo.title,
					null,
					EmbedType.VIDEO,
					null,
					0,
					null,
					null,
					null,
					new MessageEmbed.VideoInfo(trackInfo.url.toString(), 400, 300),
					null,
					null,
					null)).queue()
		}
		new CommandResultBuilder().success().build()
	}

}
