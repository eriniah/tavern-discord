package com.tavern.app.config;

public class TavernSpotifyConfig {
    private TavernSpotifyClientConfig client = new TavernSpotifyClientConfig();

    @Override
    public String toString() {
        return "TavernSpotifyConfig{" +
            "client=" + client +
            '}';
    }

    public TavernSpotifyClientConfig getClient() {
        return client;
    }

    private void setClient(TavernSpotifyClientConfig client) {
        this.client = client;
    }
}
