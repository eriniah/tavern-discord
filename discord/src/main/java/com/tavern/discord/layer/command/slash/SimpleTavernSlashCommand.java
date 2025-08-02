package com.tavern.discord.layer.command.slash;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public final class SimpleTavernSlashCommand implements TavernSlashCommand {
    private final SlashCommandData slashCommand;
    private final Class<?> commandClass;

    SimpleTavernSlashCommand(SlashCommandData slashCommand, Class<?> commandClass) {
        this.slashCommand = slashCommand;
        this.commandClass = commandClass;
    }

    @Override
    public SlashCommandData getSlashCommand() {
        return slashCommand;
    }

    public Class<?> getCommandClass() {
        return commandClass;
    }
}
