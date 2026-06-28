package com.tavern.domain.model.guild.config;

import com.tavern.domain.model.guild.GuildId;
import com.tavern.domain.model.repository.*;

public interface GuildConfigRepository extends
        GetManyRepository<GuildConfig>,
        GetOneRepository<GuildId, GuildConfig>,
        SaveRepository<GuildId, GuildConfig> {
}
