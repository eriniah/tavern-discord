package com.tavern.domain.model.discord;

public record Role(String role) implements Mentionable {
    @Override
    public String getMention() {
        return Mentionable.at(role);
    }

    @Override
    public String toString() {
        return getMention();
    }
}
