package com.tavern.discord.layer.command.slash;

import com.tavern.discord.layer.command.CommandId;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class SimpleTavernSlashCommand implements TavernSlashCommand {
    private final SlashCommandData slashCommand;
    private final Class<?> commandClass;

    SimpleTavernSlashCommand(SlashCommandData slashCommand, Class<?> commandClass) {
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
        if (getCommandId().equals(commandId)) {
            return getCommandClass();
        }
        return null;
    }

    public CommandId getCommandId() {
        return new CommandId(slashCommand.getName(), null, null);
    }

    public Class<?> getCommandClass() {
        return commandClass;
    }
}
