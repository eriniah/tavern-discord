package com.tavern.domain.model.audio.song;

import java.nio.file.Path;
import java.util.List;

public final class SavedSong implements Song {
    private final SongMetadata metadata;
    private final List<String> categories;
    // TODO: EMM Change to mongo?
    private final Path songPath;

    public SavedSong(SongMetadata metadata, List<String> categories, Path songPath) {
        this.metadata = metadata;
        this.categories = categories;
        this.songPath = songPath;
    }

    @Override
    public void play(SongPlayer player) {
        player.play(this);
    }

}
