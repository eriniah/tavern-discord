package com.tavern.domain.model.audio.song;

public interface SongPlayer {
    void play(SavedSong song);
    void play(YoutubeSong song);
    void play(YoutubeSearchSong song);
}
