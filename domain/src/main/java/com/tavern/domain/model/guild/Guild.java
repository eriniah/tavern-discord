package com.tavern.domain.model.guild;

import com.tavern.domain.model.audio.GuildAudioPlayer;
import com.tavern.domain.model.guild.config.GuildConfig;
import jakarta.annotation.Nullable;

import java.util.Optional;

/**
 * Root aggregate for all Guild related services and data
 */
public final class Guild {
    private final GuildId guildId;
    private final GuildConfig guildConfig;
    @Nullable
    private final GuildAudioPlayer audioPlayer;

    public Guild(GuildId guildId, GuildConfig guildConfig, @Nullable GuildAudioPlayer audioPlayer) {
        this.guildId = guildId;
        this.guildConfig = guildConfig;
        this.audioPlayer = audioPlayer;
    }

    public GuildId getGuildId() {
        return guildId;
    }

    public GuildConfig getGuildConfig() {
        return guildConfig;
    }

    public Optional<GuildAudioPlayer> getAudioPlayer() {
        return Optional.ofNullable(audioPlayer);
    }
}
