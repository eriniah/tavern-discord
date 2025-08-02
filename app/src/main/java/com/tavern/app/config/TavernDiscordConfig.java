package com.tavern.app.config;

import com.tavern.utilities.StringUtils;

public final class TavernDiscordConfig {
    private String token;
    private String commandPrefix;

    @Override
    public String toString() {
        return "TavernDiscordConfig{" +
            "token='" + (StringUtils.isNullOrBlank(token) ? "null" : "******") + '\'' +
            ", commandPrefix='" + commandPrefix + '\'' +
            '}';
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    private void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }
}
