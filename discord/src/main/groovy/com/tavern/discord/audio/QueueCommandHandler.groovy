package com.tavern.discord.audio

import com.tavern.discord.DiscordUtils
import com.tavern.utilities.stream.StreamChunkCollector
import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.audio.AudioService
import com.tavern.domain.model.audio.AudioTrackInfo
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import com.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.ext.XLogger
import org.slf4j.ext.XLoggerFactory
import java.time.Duration
import java.util.function.Function

import javax.annotation.Nonnull

class QueueCommandHandler implements CommandHandler {
	private static final XLogger logger = XLoggerFactory.getXLogger(QueueCommandHandler.class)
	private final AudioService audioService

	QueueCommandHandler(AudioService audioService) {
		this.audioService = audioService
	}

	@Override
	Command getCommand() {
		TavernCommands.QUEUE
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		true
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
        GuildId guildId = new GuildId(event.getGuild().getId())
		List<AudioTrackInfo> queue = audioService.getQueue(guildId)
		logger.debug("The queue has ${queue.size()} tracks")
		if (!queue.isEmpty()) {
			int songIndex = 1
			int maxOutput = 20
			Duration totalDuration = Duration.ZERO
            Duration nowPlayingTimeLeft = audioService.getNowPlaying(guildId).info.duration - audioService.getNowPlaying(guildId).getCurrentTime()

			Function<Duration, String> formatTime = (Duration duration) -> {
				String.format("%d:%2d", (duration.getSeconds()/60).intValue(), (duration.getSeconds()%60).intValue()).replace(" ", "0")
			}

			queue.stream()
					.collect(StreamChunkCollector.take(10)).forEach({ tracks ->
				StringBuilder builder = new StringBuilder()
				tracks.forEach( {track ->
						if(songIndex <= maxOutput){
							try{
								track.getUrl()
								builder.append("${songIndex++}. ")
										.append(track.title)
										.append(" - ")
										.append(DiscordUtils.escapeUrl(track.url.toString()))
										.append("\n")
							}
							catch (Exception e){
								builder.append("${songIndex++}. ")
										.append(track.title)
										.append("\n")
							}

						}
					totalDuration += track.duration
				})
				if(!builder.isBlank())
					event.getChannel().sendMessage(builder.toString()).queue()
			})
			if (queue.size() > maxOutput) {
				event.getChannel().sendMessage("+${queue.size() - maxOutput} more").queue()
			}
			event.getChannel().sendMessage("Time left in queue: ${formatTime(totalDuration)} with ${formatTime(nowPlayingTimeLeft)} left in the now playing song").queue()
		}
		new CommandResultBuilder().success().build()
	}

}
