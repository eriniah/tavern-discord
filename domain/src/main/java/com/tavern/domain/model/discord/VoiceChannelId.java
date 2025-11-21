package com.tavern.domain.model.discord;

import com.tavern.domain.model.Identifier;

public class VoiceChannelId extends Identifier implements Mentionable {
    private VoiceChannelId() { }

    public VoiceChannelId(String id) {
        super(id);
    }

    @Override
    public String getMention() {
        return Mentionable.hash(getId());
    }

    @Override
    public String toString() {
        return getMention();
    }

}
