package com.tavern.repository.mem;

import com.tavern.domain.model.guild.GuildId;
import com.tavern.domain.model.guild.config.GuildConfig;
import com.tavern.domain.model.guild.config.GuildConfigRepository;

public class GuildConfigMemRepository implements GuildConfigRepository, MemGetManyRepositoryMixin<GuildId, GuildConfig>, MemGetOneRepositoryMixin<GuildId, GuildConfig>, MemSaveRepositoryMixin<GuildId, GuildConfig> {
    private final MemDataRepositoryCache<GuildId, GuildConfig> cache = new MemDataRepositoryCache<>();

    @Override
    public MemDataRepositoryCache<GuildId, GuildConfig> getCache() {
        return cache;
    }

    @Override
    public GuildId newId() {
        throw new UnsupportedOperationException("GuildId must be supplied");
    }
}
