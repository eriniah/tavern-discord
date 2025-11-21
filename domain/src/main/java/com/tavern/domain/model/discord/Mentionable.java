package com.tavern.domain.model.discord;

/**
 * A mentionable Discord entity
 */
public interface Mentionable {
    /**
     * Returns text that mentions this entity when printed in Discord.
     * @return The mention text
     */
    String getMention();

    static String at(String id) {
        return "<@" + id + ">";
    }

    static String hash(String id) {
        return "<#" + id + ">";
    }
}
