package com.asm.tavern.discord.discord.audio


import com.asm.tavern.domain.model.TavernCommands
import com.asm.tavern.domain.model.audio.ActiveAudioTrack
import com.asm.tavern.domain.model.audio.AudioService
import com.asm.tavern.domain.model.command.*
import com.asm.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.*

import javax.annotation.Nonnull
import java.awt.Color
import java.time.Duration
import java.util.function.Function

class NowPlayingCommandHandler implements CommandHandler {
	private AudioService audioService

	NowPlayingCommandHandler(AudioService audioService) {
		this.audioService = audioService
	}

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
		ActiveAudioTrack track = audioService.getNowPlaying(new GuildId(event.getGuild().getId()))

		Function<Duration, String> formatTime = (Duration duration) -> {
			String.format("%d:%2d", (duration.getSeconds()/60).intValue(), (duration.getSeconds()%60).intValue()).replace(" ", "0")
		}

		Function<String, String> getVideoImageID = (String videoUrl) -> {
			String videoImageId = videoUrl.split("(?<=watch\\?v=)")[1]
			videoUrl = String.format("https://img.youtube.com/vi/%s/sddefault.jpg", videoImageId)
		}

		if (track) {
			String videoImgUrl = getVideoImageID(track.info.url.toString())
			EmbedBuilder eb = new EmbedBuilder()
			//eb.setTitle(track.info.title, track.info.url.toString()) // large hyperlink
			eb.setAuthor(track.info.author, track.info.url.toString()) // , videoImgUrl) // image for author top left
			//eb.setImage(videoImgUrl) // Bottom large image
			eb.setThumbnail(videoImgUrl) //Top right corner image
			eb.setDescription("Now Playing: ${track.info.title}")
			eb.addField("Duration:", "${formatTime(track.currentTime)}/${formatTime(track.info.duration)}", false)
			eb.setColor(0x5865F2) // blurple

			event.getChannel().sendMessageEmbeds(eb.build())
					.setActionRow(
							Button.primary("skip", "Skip"),
							Button.primary("shuffle", "Shuffle"),
							Button.primary("pause", "Play/Pause"),
					)
					.queue()
		}
		new CommandResultBuilder().success().build()
	}

}
