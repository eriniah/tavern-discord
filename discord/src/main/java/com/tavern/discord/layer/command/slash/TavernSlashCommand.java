package com.tavern.discord.layer.command.slash;

import com.tavern.discord.layer.command.CommandId;
import jakarta.annotation.Nullable;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public sealed interface TavernSlashCommand permits SimpleTavernSlashCommand, StructuredTavernSlashCommand {
    String getCommandName();
    SlashCommandData getSlashCommand();
    void visit(TavernSlashCommandVisitor visitor);
    @Nullable
    Class<?> getCommandClass(CommandId commandId);
    boolean isValid(CommandId commandId);
}
