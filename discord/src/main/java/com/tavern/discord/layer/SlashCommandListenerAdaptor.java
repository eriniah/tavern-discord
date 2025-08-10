package com.tavern.discord.layer;

import com.tavern.discord.layer.annotations.Context;
import com.tavern.discord.layer.annotations.Inject;
import com.tavern.discord.layer.command.CommandId;
import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandCreator;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandHandler;
import com.tavern.utilities.*;
import com.tavern.utilities.convert.TypeConverterRegistries;
import com.tavern.utilities.convert.TypeConverterRegistry;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.lang.reflect.*;
import java.util.*;

class SlashCommandListenerAdaptor extends ListenerAdapter {
    private static final XLogger logger = XLoggerFactory.getXLogger(SlashCommandListenerAdaptor.class);

    private final Injectables injectables;
    private final TypeConverterRegistry typeConverter;
    private final TavernSlashCommandCache commandCache;

    public SlashCommandListenerAdaptor(Injectables injectables, TypeConverterRegistry typeConverter, TavernSlashCommandCache commandCache) {
        this.injectables = injectables;
        this.typeConverter = typeConverter;
        this.commandCache = commandCache;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (null == event.getGuild()) {
            // Ignore attempted non-guild interactions
            logger.trace("Ignoring non-guild slash command interaction: {}", event);
            event.reply("Tavern only accepts commands from a guild")
                .setEphemeral(true)
                .queue();
            return;
        }

        CommandId commandId = new CommandId(event.getName(), event.getSubcommandGroup(), event.getSubcommandName());

        TavernSlashCommand command = commandCache.getCommand(event.getCommandId());
        if (null == command) {
            logger.trace("Ignoring unknown slash command: {}", event.getCommandId());
            event.reply(String.format("Unknown command '%s'", event.getCommandId()))
                .setEphemeral(true)
                .queue();
            return;
        }

        Class<?> commandClass = command.getCommandClass(commandId);
        if (null == commandClass) {
            logger.trace("No command class for command: {}", commandId);
            event.reply(String.format("Unknown command '%s'", commandId))
                .setEphemeral(true)
                .queue();
            return;
        }

        // Create command instance
        for (Constructor<?> constructor: commandClass.getConstructors()) {
            if (null != constructor.getAnnotation(SlashCommandCreator.class)) {

            }
        }

        // Locate listener method
        SlashCommandListener listener = commandCache.getListener(commandId);
        if (null == listener) {
            logger.trace("No listener for command: {}", commandId);
            event.reply(String.format("Unknown command '%s'", commandId))
                .setEphemeral(true)
                .queue();
            return;
        }

        Method[] listenerMethods = listener.getClass().getMethods();
        for (Method listenerMethod: listenerMethods) {
            SlashCommandHandler handler = listenerMethod.getAnnotation(SlashCommandHandler.class);
            if (null == handler) {
                continue;
            }

            Parameter[] parameters = listenerMethod.getParameters();
            for (Parameter parameter: parameters) {
                Context context = parameter.getAnnotation(Context.class);
                if (null != context) {
                    // TODO: EMM Handle context injection
                    continue;
                }

                Inject inject = parameter.getAnnotation(Inject.class);
                if (null != inject) {
                    // TODO: EMM Handle Inject injection
                    continue;
                }

                if (commandClass.equals(parameter.getType())) {
                    commandClass.getMethods()
                }
            }
        }

        // TODO: EMM
        //       Do magical lookups
        //       Accept the injectables and attempt to populate
        //       Instantiate command
    }

}
