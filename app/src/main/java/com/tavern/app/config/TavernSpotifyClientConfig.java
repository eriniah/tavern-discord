package com.tavern.app.config;

import com.tavern.utilities.StringUtils;

public class TavernSpotifyClientConfig {
    private String id;
    private String secret;

    @Override
    public String toString() {
        return "TavernSpotifyClientConfig{" +
            "id='" + id + '\'' +
            ", secret='" + (StringUtils.isNullOrBlank(secret) ? "null" : "******") + '\'' +
            '}';
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    private void setSecret(String secret) {
        this.secret = secret;
    }
}
