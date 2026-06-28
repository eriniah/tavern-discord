package com.tavern.domain.model.guild.config;

import com.tavern.domain.model.discord.ChannelId;
import com.tavern.domain.model.discord.Role;

public final class DrinkConfig {
    private Role role;
    private ChannelId campaignChannelId;

    public DrinkConfig(Role role, ChannelId campaignChannelId) {
        this.role = role;
        this.campaignChannelId = campaignChannelId;
    }

    @Override
    public String toString() {
        return "DrinkConfig{" +
            "role=" + role +
            '}';
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ChannelId getCampaignChannelId() {
        return campaignChannelId;
    }

    public void setCampaignChannelId(ChannelId campaignChannelId) {
        this.campaignChannelId = campaignChannelId;
    }
}
