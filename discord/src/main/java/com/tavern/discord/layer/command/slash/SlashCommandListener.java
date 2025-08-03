package com.tavern.discord.layer.command.slash;

/**
 * Defines and listens for event for a specific slash command
 */
public interface SlashCommandListener {
    /**
     * Create and return the slash command that this listener handles
     * @param factory The factory instance for creating slash commands
     * @return The slash command
     */
    TavernSlashCommand getCommand(TavernSlashCommandFactory factory);
}
