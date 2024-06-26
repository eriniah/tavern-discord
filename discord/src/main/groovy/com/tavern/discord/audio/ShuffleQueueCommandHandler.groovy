package com.tavern.discord.audio

import com.tavern.domain.model.TavernCommands
import com.tavern.domain.model.audio.AudioService
import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage
import com.tavern.domain.model.command.CommandHandler
import com.tavern.domain.model.command.CommandMessage
import com.tavern.domain.model.command.CommandResult
import com.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

import javax.annotation.Nonnull

class ShuffleQueueCommandHandler implements CommandHandler {
    private final AudioService audioService

    ShuffleQueueCommandHandler(AudioService audioService) {
        this.audioService = audioService
    }

    @Override
    Command getCommand() {
        TavernCommands.SHUFFLE
    }

    @Override
    boolean supportsUsage(CommandArgumentUsage usage) {
        true
    }

    @Override
    CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
        audioService.shuffle(new GuildId(event.guild.id))
        event.getChannel().sendMessage("Shuffled the queue!").queue()
    }
}
