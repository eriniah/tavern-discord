package com.tavern.discord.audio

import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.audio.AudioService
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.command.CommandResultBuilder
import com.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull

class SkipTimeCommandHandler implements CommandHandler {
    private final AudioService audioService

    SkipTimeCommandHandler(AudioService audioService) {
        this.audioService = audioService
    }

    @Override
    Command getCommand() {
        TavernCommands.SKIP_TIME
    }

    @Override
    boolean supportsUsage(CommandArgumentUsage usage) {
        true
    }

    @Override
    CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
        int skipTime = message.args[0] ? Integer.parseInt(message.args[0]) : 60
        GuildId guildId = new GuildId(event.getGuild().getId())
        String title = audioService.getNowPlaying(guildId).info.title
        event.getChannel().sendMessage("Skipping ${skipTime} seconds from ${title}").queue()



        audioService.skipTime(guildId, skipTime)
        new CommandResultBuilder().success().build()
    }

}
