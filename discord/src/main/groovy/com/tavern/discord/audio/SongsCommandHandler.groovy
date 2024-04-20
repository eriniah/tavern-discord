package com.tavern.discord.audio

import com.tavern.discord.DiscordUtils
import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.audio.Song
import com.tavern.domain.model.audio.SongService
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.ext.XLogger
import org.slf4j.ext.XLoggerFactory

import javax.annotation.Nonnull
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.StreamSupport

class SongsCommandHandler implements CommandHandler {
	private static final XLogger logger = XLoggerFactory.getXLogger(SongsCommandHandler.class)
	private final SongService songService

	SongsCommandHandler(SongService songService) {
		this.songService = songService
	}

	@Override
	Command getCommand() {
		TavernCommands.SONGS
	}

	@Override
	boolean supportsUsage(CommandArgumentUsage usage) {
		TavernCommands.SongsUsages.DEFAULT == usage
	}

	@Override
	CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		int chunkSize = 10
		String currentCategory = ""

		// Function for pushing message so message can be pushed when max chunk size to avoid discord max message length. Also empty builder when pushed
		Function<StringBuilder, String> pushMessage = (StringBuilder builder) -> {
			event.getChannel().asTextChannel().sendMessageEmbeds(new MessageEmbed(null, "${currentCategory} Songs", builder.toString(), null, null, 0xFF0000, null, null, null, null, null,null, null)).queue()
			builder.setLength(0)
		}


		StreamSupport.stream(songService.getSongRegistry().getAll().spliterator(), false)
				.sorted(Comparator.comparing((Song song) -> song.id.id))
				.collect(Collectors.groupingBy(Song::getCategory))
				.values()
				.forEach({ songs ->
					int count = 0
					StringBuilder builder = new StringBuilder()
					songs.forEach({ song ->
						currentCategory = song.category
						try {
							new URL(song.uri.toString())
							builder.append("[${song.id}](${DiscordUtils.escapeUrl(song.uri.toString())})\n")
						}
						catch (Exception e) {
							logger.info("File was most likely local: " + e)
							builder.append("${song.id}\n")
						}
						if(++count == chunkSize){
							pushMessage(builder)
						}
					})
					pushMessage(builder)
				})
		new CommandResultBuilder().success().build()
	}

}
