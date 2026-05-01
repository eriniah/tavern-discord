package com.tavern.domain.model.discord.guild.config;

import com.tavern.domain.model.discord.guild.GuildId;
import com.tavern.domain.model.repository.GetManyRepository;
import com.tavern.domain.model.repository.GetOneRepository;

public interface GuildConfigRepository extends
        GetManyRepository<GuildConfig>,
        GetOneRepository<GuildId, GuildConfig> {
}
