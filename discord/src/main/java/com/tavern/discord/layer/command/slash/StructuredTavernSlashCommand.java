package com.tavern.discord.layer.command.slash;

import net.dv8tion.jda.api.interactions.commands.build.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class StructuredTavernSlashCommand implements TavernSlashCommand {
    private final SlashCommandData slashCommand;
    // subcommand name -> subcommand
    private final Map<String, TavernSubCommand> subCommands;
    // Subgroup name -> subcommand name -> subcommand
    private final Map<String, Map<String, TavernSubCommand>> subgroupCommands;

    StructuredTavernSlashCommand(SlashCommandData slashCommand, Map<String, TavernSubCommand> subCommands, Map<String, Map<String, TavernSubCommand>> subgroupCommands) {
        this.slashCommand = slashCommand;
        this.subCommands = Collections.unmodifiableMap(subCommands);
        this.subgroupCommands = Collections.unmodifiableMap(subgroupCommands.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> Collections.unmodifiableMap(entry.getValue()))
        ));
    }

    @Override
    public SlashCommandData getSlashCommand() {
        return slashCommand;
    }

    public Map<String, TavernSubCommand> getSubCommands() {
        return subCommands;
    }

    public Map<String, Map<String, TavernSubCommand>> getSubgroupCommands() {
        return subgroupCommands;
    }

    public static final class TavernSubCommand {
        private final SubcommandData subcommandData;
        private final Class<?> commandClass;

        TavernSubCommand(SubcommandData subcommandData, Class<?> commandClass) {
            this.subcommandData = subcommandData;
            this.commandClass = commandClass;
        }

        public SubcommandData getSubcommandData() {
            return subcommandData;
        }

        public Class<?> getCommandClass() {
            return commandClass;
        }
    }

}
