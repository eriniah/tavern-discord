package com.tavern.discord.layer.command.slash;

import com.tavern.utilities.CollectionUtils;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Discord commands that have no 'base command' but are instead made up of sub commands and/or subgroups
 * Ex:
 * command
 *  - subcommand1
 *  - subcommand2
 *  - subgroup1
 *    - sgcommand1
 */
public final class StructuredTavernSlashCommand implements TavernSlashCommand {
    private final SlashCommandData slashCommand;
    // subcommand name -> subcommand
    private final Map<String, TavernSubCommand> subCommands;
    // Subgroup name -> subgroup
    private final Map<String, TavernCommandSubgroup> subgroups;

    StructuredTavernSlashCommand(SlashCommandData slashCommand, Map<String, TavernSubCommand> subCommands, Map<String, TavernCommandSubgroup> subgroups) {
        this.slashCommand = slashCommand;
        this.subCommands = CollectionUtils.wrapIfPresent(subCommands, HashMap::new);
        this.subgroups = CollectionUtils.wrapIfPresent(subgroups, HashMap::new);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TavernSlashCommand that = (TavernSlashCommand) o;
        return Objects.equals(slashCommand.getName(), that.getSlashCommand().getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(slashCommand.getName());
    }

    @Override
    public SlashCommandData getSlashCommand() {
        return slashCommand;
    }

    /**
     * Unmodifiable map of subcommands
     * @return sub commands
     */
    public Map<String, TavernSubCommand> getSubCommands() {
        return Collections.unmodifiableMap(subCommands);
    }

    /**
     * Unmodifiable map of subgroups
     * @return subgroups
     */
    public Map<String, TavernCommandSubgroup> getSubgroups() {
        return Collections.unmodifiableMap(subgroups);
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

    public static final class TavernCommandSubgroup {
        private final SubcommandGroupData subcommandGroupData;
        private final Map<String, TavernSubCommand> subCommands;

        TavernCommandSubgroup(SubcommandGroupData subcommandGroupData, Map<String, TavernSubCommand> subCommands) {
            this.subcommandGroupData = subcommandGroupData;
            this.subCommands = CollectionUtils.wrapIfPresent(subCommands, HashMap::new);
        }

        public SubcommandGroupData getSubcommandGroupData() {
            return subcommandGroupData;
        }

        public Map<String, TavernSubCommand> getSubCommands() {
            return Collections.unmodifiableMap(subCommands);
        }
    }

}
