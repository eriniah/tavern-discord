package com.tavern.domain.model.discord.guild.config;

import com.tavern.domain.model.discord.Role;

public final class DrinkConfig {
    private Role role;

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
}
