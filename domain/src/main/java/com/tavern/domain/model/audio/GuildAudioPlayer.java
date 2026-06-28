package com.tavern.domain.model.audio;

import com.tavern.domain.model.audio.song.Song;
import com.tavern.domain.model.audio.song.SongMetadata;

public interface GuildAudioPlayer {
    void stop();
    void pause();
    void resume();
    void skip(int skip);
    void remove(int start, int end);

    SongMetadata play(Song song);
    SongMetadata playNow(Song song);
    SongMetadata nowPlaying(Song song);

    void weave(Song song);
    void playMode(String category);
    void shuffle(ShuffleOption shuffleOption);
    void repeat(RepeatOption repeatOption);

}
