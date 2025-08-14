package com.tavern.discord.layer.command.slash;

import com.tavern.discord.layer.command.CommandId;
import com.tavern.utilities.CollectionUtils;
import jakarta.annotation.Nullable;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.util.*;

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
    private final Map<String, TavernCommandSubgroup> subGroups;
    private final Map<CommandId, Class<?>> commandToDataClass;

    StructuredTavernSlashCommand(SlashCommandData slashCommand, Map<String, TavernSubCommand> subCommands, Map<String, TavernCommandSubgroup> subGroups) {
        this.slashCommand = slashCommand;
        this.subCommands = CollectionUtils.wrapIfPresent(subCommands, HashMap::new);
        this.subGroups = CollectionUtils.wrapIfPresent(subGroups, HashMap::new);

        // Note: Important that we use a Map implementation that supports null values. Commands without a command class
        // need to pass a containsKey() check
        this.commandToDataClass = new HashMap<>();
        subCommands.forEach((name, command) -> {
            commandToDataClass.put(
                new CommandId(slashCommand.getName(), null, name),
                command.getCommandClass()
            );
        });
        subGroups.forEach((subGroupName, subGroup) -> {
            subGroup.getSubCommands().forEach((name, command) -> {
                commandToDataClass.put(
                    new CommandId(slashCommand.getName(), subGroupName, name),
                    command.getCommandClass()
                );
            });
        });
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
    public String getCommandName() {
        return slashCommand.getName();
    }

    @Override
    public SlashCommandData getSlashCommand() {
        return slashCommand;
    }

    @Override
    public void visit(TavernSlashCommandVisitor visitor) {
        visitor.visit(this);
    }

    @Nullable
    @Override
    public Class<?> getCommandClass(CommandId commandId) {
        return commandToDataClass.get(commandId);
    }

    @Override
    public boolean isValid(CommandId commandId) {
        return commandToDataClass.containsKey(commandId);
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
    public Map<String, TavernCommandSubgroup> getSubGroups() {
        return Collections.unmodifiableMap(subGroups);
    }

    public static final class TavernSubCommand {
        private final SubcommandData subCommandData;
        @Nullable
        private final Class<?> commandClass;

        TavernSubCommand(SubcommandData subCommandData, Class<?> commandClass) {
            this.subCommandData = subCommandData;
            this.commandClass = commandClass;
        }

        public SubcommandData getSubCommandData() {
            return subCommandData;
        }

        @Nullable
        public Class<?> getCommandClass() {
            return commandClass;
        }
    }

    public static final class TavernCommandSubgroup {
        private final SubcommandGroupData subCommandGroupData;
        private final Map<String, TavernSubCommand> subCommands;

        TavernCommandSubgroup(SubcommandGroupData subCommandGroupData, Map<String, TavernSubCommand> subCommands) {
            this.subCommandGroupData = subCommandGroupData;
            this.subCommands = CollectionUtils.wrapIfPresent(subCommands, HashMap::new);
        }

        public SubcommandGroupData getSubCommandGroupData() {
            return subCommandGroupData;
        }

        public Map<String, TavernSubCommand> getSubCommands() {
            return Collections.unmodifiableMap(subCommands);
        }
    }

}
