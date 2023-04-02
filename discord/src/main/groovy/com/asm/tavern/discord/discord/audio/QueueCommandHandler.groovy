package com.asm.tavern.discord.discord.audio

import com.asm.tavern.discord.utilities.DiscordUtils
import com.asm.tavern.discord.utilities.stream.StreamChunkCollector
import com.asm.tavern.domain.model.TavernCommands
import com.asm.tavern.domain.model.audio.AudioService
import com.asm.tavern.domain.model.audio.AudioTrackInfo
import com.asm.tavern.domain.model.command.*
import com.asm.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
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
		List<AudioTrackInfo> queue = audioService.getQueue(new GuildId(event.getGuild().getId()))
		logger.debug("The queue has ${queue.size()} tracks")
		if (!queue.isEmpty()) {
			int songIndex = 1
			int maxOutput = 20
			Duration totalDuration = Duration.ZERO

			Function<Duration, String> formatTime = (Duration duration) -> {
				String.format("%d:%2d", (duration.getSeconds()/60).intValue(), (duration.getSeconds()%60).intValue()).replace(" ", "0")
			}

			queue.stream()
					.collect(StreamChunkCollector.take(10)).forEach({ tracks ->
				StringBuilder builder = new StringBuilder()
				tracks.forEach( {track ->
						if(songIndex <= maxOutput){
							builder.append("${songIndex++}. ")
									.append(track.title)
									.append(" - ")
									.append(DiscordUtils.escapeUrl(track.url.toString()))
									.append("\n")
						}
					totalDuration += track.duration
				})
				if(!builder.isBlank())
					event.getChannel().sendMessage(builder.toString()).queue()
			})
			if (queue.size() > maxOutput) {
				event.getChannel().sendMessage("+${queue.size() - maxOutput} more").queue()
			}
			event.getChannel().sendMessage("Remaining Duration: ${formatTime(totalDuration)}").queue()
		}


		List<SelectOption> selectOptionList = new ArrayList<SelectOption>()
		int position = 0
		for(AudioTrackInfo trackInfo : queue.toList()){
			if(position >= 23)
				break
			selectOptionList.add(new SelectOption(trackInfo.title, "queuePosition_" + position))
			position++
		}

		EmbedBuilder eb = new EmbedBuilder()
		eb.setAuthor("Queue")
		eb.setDescription("Select song from queue to modify")
		eb.setColor(0x5865F2)
		event.getChannel().sendMessageEmbeds(eb.build())
				.addActionRow(
						StringSelectMenu.create("choose-song")
								.addOption("Previous 25", "previousQueue")
								.addOptions(selectOptionList)
								.addOption("Next 25", "nextQueue")
								.build())
				.queue()
		
		new CommandResultBuilder().success().build()
	}

}
