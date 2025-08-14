package com.tavern.discord.layer;

import com.tavern.discord.layer.command.CommandId;
import com.tavern.discord.layer.command.slash.*;
import jakarta.annotation.Nullable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

final class TavernSlashCommandCache {
    private static final XLogger logger = XLoggerFactory.getXLogger(TavernSlashCommandCache.class);
    
    private final Map<String, CachedCommandHandler> nameToSlashCommand;
    private final TavernSlashCommandFactory factory;

    TavernSlashCommandCache(TavernSlashCommandFactory factory, Collection<Class<? extends SlashCommandListener>> slashCommandListeners) {
        this.factory = factory;
        this.nameToSlashCommand = slashCommandListeners.stream()
            .map(command -> {
                TavernSlashCommand slashCommand;
                try {
                    slashCommand = command.getConstructor().newInstance().getCommand(factory);
                } catch (NoSuchMethodException ex) {
                    throw new IllegalArgumentException(String.format("SlashCommandListener '%s' must have a default constructor.", command.getSimpleName()), ex);
                } catch (Exception ex) {
                    throw new IllegalArgumentException(String.format("Failed to construct listener '%s'.", command.getSimpleName()), ex);
                }

                return new CachedCommandHandler(slashCommand, command);
            })
            .collect(Collectors.toMap(
                command -> command.command.getSlashCommand().getName(),
                Function.identity()
        ));
    }

    @Nullable
    public TavernSlashCommand getCommand(String name) {
        if (!nameToSlashCommand.containsKey(name)) {
            return null;
        }
        return nameToSlashCommand.get(name).command;
    }

    @Nullable
    public SlashCommandListener createListener(CommandId commandId) {
        if (!nameToSlashCommand.containsKey(commandId.command())) {
            return null;
        }
        return nameToSlashCommand.get(commandId.command()).createListener();
    }

    void postCommands(JDA jda) {
        jda.updateCommands().addCommands(
            nameToSlashCommand.values().stream()
                .map(c -> c.command.getSlashCommand())
                .collect(Collectors.toList())
        ).queue(
            commands -> logger.info("Updated discord slash commands: {}", commands.stream().map(Command::getName).collect(Collectors.joining(", "))),
            ex -> logger.error("Failed to update discord slash commands", ex)
        );
    }

    static class CachedCommandHandler {
        private final TavernSlashCommand command;
        private final Class<? extends SlashCommandListener> listenerClass;

        public CachedCommandHandler(TavernSlashCommand command, Class<? extends SlashCommandListener> listenerClass) {
            this.command = command;
            this.listenerClass = listenerClass;
        }

        public SlashCommandListener createListener() {
            try {
                return listenerClass.getConstructor().newInstance();
            } catch (Exception ex) {
                throw new IllegalStateException(String.format("Failed to create listener instance for command '%s'", command.getSlashCommand().getName()), ex);
            }
        }
    }
}
