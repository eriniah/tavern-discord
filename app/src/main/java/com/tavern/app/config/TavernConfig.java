package com.tavern.app.config;

public final class TavernConfig {
    private TavernDiscordConfig discord;
    private TavernSpotifyConfig spotify;

    @Override
    public String toString() {
        return "TavernConfig{" +
            "discord=" + discord +
            ", spotify=" + spotify +
            '}';
    }

    public TavernDiscordConfig getDiscord() {
        return discord;
    }

    private void setDiscord(TavernDiscordConfig discord) {
        this.discord = discord;
    }

    public TavernSpotifyConfig getSpotify() {
        return spotify;
    }

    private void setSpotify(TavernSpotifyConfig spotify) {
        this.spotify = spotify;
    }
}
