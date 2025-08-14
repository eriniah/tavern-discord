package com.tavern.discord.layer.command.slash;

import com.tavern.discord.layer.command.CommandId;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import jakarta.annotation.Nullable;

import java.util.Objects;

public final class SimpleTavernSlashCommand implements TavernSlashCommand {
    private final SlashCommandData slashCommand;
    @Nullable
    private final Class<?> commandClass;

    SimpleTavernSlashCommand(SlashCommandData slashCommand) {
        this(slashCommand, null);
    }

    SimpleTavernSlashCommand(SlashCommandData slashCommand, @Nullable Class<?> commandClass) {
        this.slashCommand = slashCommand;
        this.commandClass = commandClass;
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
        if (isValid(commandId)) {
            return getCommandClass();
        }
        return null;
    }

    @Override
    public boolean isValid(CommandId commandId) {
        return getCommandId().equals(commandId);
    }

    public CommandId getCommandId() {
        return new CommandId(slashCommand.getName(), null, null);
    }

    @Nullable
    public Class<?> getCommandClass() {
        return commandClass;
    }
}
