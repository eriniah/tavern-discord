package com.tavern.domain.model.guild;

import com.tavern.domain.model.audio.GuildAudioPlayer;
import com.tavern.domain.model.discord.DiscordInterface;
import com.tavern.domain.model.guild.config.*;
import com.tavern.domain.model.repository.GetOptions;

import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public final class GuildService {
    private final DiscordInterface discordInterface;
    private final GuildConfigRepository guildConfigRepository;
    private final GuildServiceCache<GuildAudioPlayer> audioPlayerCache;

    public GuildService(DiscordInterface discordInterface, GuildConfigRepository guildConfigRepository, GuildServiceCache<GuildAudioPlayer> audioPlayerCache) {
        this.discordInterface = discordInterface;
        this.guildConfigRepository = guildConfigRepository;
        this.audioPlayerCache = audioPlayerCache;
    }

    public Set<GuildId> getInitializedGuilds() {
        return guildConfigRepository.getMany(new GetOptions()).into(new LinkedList<>()).stream()
            .map(GuildConfig::getId)
            .collect(Collectors.toSet());
    }

    public Guild get(GuildId guildId) {
        GuildConfig config = guildConfigRepository.get(guildId).orElseGet(() -> {
            GuildConfig newConfig = discordInterface.newGuild(guildId);
            guildConfigRepository.save(newConfig);
            return newConfig;
        });

        return new Guild(guildId, config, audioPlayerCache.getIfExists(guildId));
    }

}
