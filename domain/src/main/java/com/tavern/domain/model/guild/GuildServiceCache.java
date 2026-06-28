package com.tavern.domain.model.guild;

import com.tavern.domain.model.discord.UserId;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class GuildServiceCache<Service> {
    private final Map<GuildId, Service> serviceMap = new HashMap<>();
    private final BiFunction<GuildId, UserId, Service> serviceCreator;

    public GuildServiceCache(BiFunction<GuildId, UserId, Service> serviceCreator) {
        this.serviceCreator = serviceCreator;
    }

    public Service getOrCreate(GuildId guildId, UserId userId) {
        return serviceMap.computeIfAbsent(guildId, id -> serviceCreator.apply(id, userId));
    }

    @Nullable
    public Service getIfExists(GuildId guildId) {
        return serviceMap.get(guildId);
    }
}
