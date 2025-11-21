package com.tavern.domain.model.discord.guild;

import com.tavern.domain.model.discord.guild.config.GuildConfigRepository;

public final class GuildService {
    private final GuildConfigRepository guildConfigRepository;

    public GuildService(GuildConfigRepository guildConfigRepository) {
        this.guildConfigRepository = guildConfigRepository;
    }

}
