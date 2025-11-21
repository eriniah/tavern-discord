package com.tavern.domain.model.discord.guild.config;

import com.tavern.domain.model.IdentifiedDomainObject;
import com.tavern.domain.model.discord.guild.GuildId;

/**
 * All Configuration for a single guild
 */
public final class GuildConfig extends IdentifiedDomainObject<GuildId> {
    private final DrinkConfig drinkConfig;

    public GuildConfig(GuildId guildId, DrinkConfig drinkConfig) {
        super(guildId);
        this.drinkConfig = drinkConfig;
    }

    public DrinkConfig getDrinkConfig() {
        return drinkConfig;
    }
}
