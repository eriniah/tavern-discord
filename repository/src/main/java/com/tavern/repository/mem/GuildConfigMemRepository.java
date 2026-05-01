package com.tavern.repository.mem;

import com.tavern.domain.model.discord.guild.GuildId;
import com.tavern.domain.model.discord.guild.config.GuildConfig;
import com.tavern.domain.model.discord.guild.config.GuildConfigRepository;

public class GuildConfigMemRepository implements GuildConfigRepository, MemGetManyRepositoryMixin<GuildId, GuildConfig>, MemGetOneRepositoryMixin<GuildId, GuildConfig> {
    private final MemDataRepositoryCache<GuildId, GuildConfig> cache = new MemDataRepositoryCache<>();

    @Override
    public MemDataRepositoryCache<GuildId, GuildConfig> getCache() {
        return cache;
    }

}
