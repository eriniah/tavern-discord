package com.asm.tavern.discord.discord.audio


import com.asm.tavern.domain.model.TavernCommands
import com.asm.tavern.domain.model.audio.ActiveAudioTrack
import com.asm.tavern.domain.model.audio.AudioService
import com.asm.tavern.domain.model.command.*
import com.asm.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.*
import org.slf4j.ext.XLogger
import org.slf4j.ext.XLoggerFactory

import javax.annotation.Nonnull
import java.awt.Color
import java.time.Duration
import java.util.function.Function

class NowPlayingCommandHandler implements CommandHandler {
	private static final XLogger logger = XLoggerFactory.getXLogger(NowPlayingCommandHandler.class)
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
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		ActiveAudioTrack track = audioService.getNowPlaying(new GuildId(event.getGuild().getId()))

		Function<Duration, String> formatTime = (Duration duration) -> {
			String.format("%d:%2d", (duration.getSeconds()/60).intValue(), (duration.getSeconds()%60).intValue()).replace(" ", "0")
		}

		Function<String, String> getVideoImageID = (String videoUrl) -> {
			try{
				String videoImageId = videoUrl.split("(?<=watch\\?v=)")[1]
				videoUrl = String.format("https://img.youtube.com/vi/%s/sddefault.jpg", videoImageId)
			}
			catch (Exception e){
				logger.info("No VideoImage Found" + e)
			}

		}

		EmbedBuilder eb = new EmbedBuilder()
		if(track) {
			try {
				String videoImgUrl = getVideoImageID(track.info.url.toString())
				eb.setTitle(track.info.title, track.info.url.toString()) // large hyperlink
				//eb.setAuthor(track.info.author, track.info.url.toString()) // , videoImgUrl) image for author top left
				//eb.setImage(videoImgUrl) // Bottom large image
				eb.setThumbnail(videoImgUrl) // Top right corner image
			}
			catch (Exception e) {
				eb.setTitle(track.info.title)
				logger.info("Video Image was unable to be fetched: " + e)
			}
            eb.setDescription("By: ${track.info.author}")
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
