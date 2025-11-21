package com.tavern.domain.model.discord;

import com.tavern.domain.model.Identifier;

public class UserId extends Identifier implements Mentionable {
    private UserId() { }

    public UserId(String id) {
        super(id);
    }

    @Override
    public String getMention() {
        return Mentionable.at(getId());
    }

    @Override
    public String toString() {
        return getMention();
    }

}
