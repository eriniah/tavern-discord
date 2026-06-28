package com.tavern.domain.model.audio.song;

public interface SongMetadata {
    String getId();
    String getTitle();
    String getArtist();
    long getLength();
    boolean isStream();
    String getUri();
    String getArtworkUrl();
}
