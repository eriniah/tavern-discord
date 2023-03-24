package com.asm.tavern.discord.discord.audio

import com.asm.tavern.domain.model.TavernCommands
import com.asm.tavern.domain.model.audio.SongId
import com.asm.tavern.domain.model.audio.SongService
import com.asm.tavern.domain.model.command.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull

class AddSongWithCategoryCommandHandler implements CommandHandler {
    private final SongService songService

    AddSongWithCategoryCommandHandler(SongService songService) {
        this.songService = songService
    }

    @Override
    Command getCommand() {
        TavernCommands.SONGS
    }

    @Override
    boolean supportsUsage(CommandArgumentUsage usage) {
        TavernCommands.SongsUsages.ADD_WITH_CATEGORY == usage
    }

    @Override
    CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
        String id = message.args[1]
        String uri = message.args[2]
        String category = message.args[3]

        songService.register(new SongId(id), new URI(uri), category)
        event.getChannel().sendMessage("Registered new song ${id} under category: ${category}").queue()
        new CommandResultBuilder().success().build()
    }

}
