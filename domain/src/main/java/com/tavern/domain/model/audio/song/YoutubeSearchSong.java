package com.tavern.domain.model.audio.song;

public final class YoutubeSearchSong implements Song {
    private final String query;

    public YoutubeSearchSong(String query) {
        this.query = query;
    }

    @Override
    public void play(SongPlayer player) {
        player.play(this);
    }
}
