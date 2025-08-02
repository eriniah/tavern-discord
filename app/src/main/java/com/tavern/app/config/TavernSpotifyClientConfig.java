package com.tavern.app.config;

public class TavernSpotifyClientConfig {
    private String id;
    private String secret;

    @Override
    public String toString() {
        return "TavernSpotifyClientConfig{" +
            "id='" + id + '\'' +
            ", secret='" + secret + '\'' +
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
