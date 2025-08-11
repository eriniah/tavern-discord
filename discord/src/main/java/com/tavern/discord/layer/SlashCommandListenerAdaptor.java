package com.tavern.discord.layer;

import com.tavern.discord.layer.annotations.Context;
import com.tavern.discord.layer.annotations.Inject;
import com.tavern.discord.layer.command.CommandId;
import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.*;
import com.tavern.domain.model.discord.GuildId;
import com.tavern.utilities.convert.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

class SlashCommandListenerAdaptor extends ListenerAdapter {
    private static final XLogger logger = XLoggerFactory.getXLogger(SlashCommandListenerAdaptor.class);

    private final Injectables injectables;
    private final TypeConverterRegistry optionTypeConverter;
    private final TavernSlashCommandCache commandCache;
    private final TypeConverterRegistry returnTypeConverter;

    public SlashCommandListenerAdaptor(Injectables injectables, TypeConverterRegistry optionTypeConverter, TavernSlashCommandCache commandCache) {
        this.injectables = injectables;
        this.optionTypeConverter = optionTypeConverter;
        this.commandCache = commandCache;

        this.returnTypeConverter = TypeConverterRegistries.ofConvertersBuilder()
            .add(TypeConverter.of(MessageCreateData.class, MessageCreateData.class, Function.identity()))
            .add(TypeConverter.of(String.class, MessageCreateData.class, MessageCreateData::fromContent))
            .add(TypeConverter.of(MessageEditData.class, MessageCreateData.class, MessageCreateData::fromEditData))
            .add(TypeConverter.of(MessageEmbed.class, MessageCreateData.class, MessageCreateData::fromEmbeds))
            .add(TypeConverter.of(FileUpload.class, MessageCreateData.class, MessageCreateData::fromFiles))
            .build();
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

        TavernSlashCommand command = commandCache.getCommand(commandId.command());
        if (null == command) {
            logger.trace("Ignoring unknown slash command: {}", commandId.command());
            event.reply(String.format("Unknown command '%s'", commandId.command()))
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

        // Locate listener method
        SlashCommandListener listener = commandCache.createListener(commandId);
        if (null == listener) {
            logger.trace("No listener for command: {}", commandId);
            event.reply(String.format("Unknown command '%s'", commandId))
                .setEphemeral(true)
                .queue();
            return;
        }

        Injectables contextuals = createContext(event);

        // Inject into class fields
        for (Field field: listener.getClass().getDeclaredFields()) {
            Inject inject = field.getAnnotation(Inject.class);
            Context context = field.getAnnotation(Context.class);

            if (null != inject) {
                try {
                    if (field.canAccess(listener) || field.trySetAccessible()) {
                        field.set(listener, injectables.get(field.getType()));
                    }
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Failed to inject field " + field.getName(), ex);
                }
            } else if (null != context) {
                try {
                    if (field.canAccess(listener) || field.trySetAccessible()) {
                        field.set(listener, contextuals.get(field.getType()));
                    }
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Failed to inject context field " + field.getName(), ex);
                }
            }
        }

        Object commandInstance = createCommandInstance(event, commandClass);

        // Locate command method
        Method commandMethod = Arrays.stream(listener.getClass().getMethods())
            .filter(method -> {
                SlashCommandHandler handler = method.getAnnotation(SlashCommandHandler.class);
                return null != handler
                    && Arrays.stream(method.getParameterTypes())
                        .anyMatch(paramType -> paramType.equals(commandClass));
            }).findFirst().orElseThrow(() -> new IllegalStateException("Failed to locate command method for " + commandId));

        // Build method parameters
        Object[] commandParameters = Arrays.stream(commandMethod.getParameters())
            .map(parameter -> {
                Inject inject = parameter.getAnnotation(Inject.class);
                Context context = parameter.getAnnotation(Context.class);

                if (null != inject) {
                    return injectables.get(parameter.getType());
                } else if (null != context) {
                    return contextuals.get(parameter.getType());
                } else if (commandClass.equals(parameter.getType())) {
                    return commandInstance;
                } else {
                    logger.trace("Unknown mapping for parameter type '{}'", parameter.getType().getSimpleName());
                    return null;
                }
            }).toArray();

        try {
            Object ret = commandMethod.invoke(listener, commandParameters);

            if (!void.class.equals(commandMethod.getReturnType()) && null != ret) {
                // Attempt to convert non-collection types with the type converter
                MessageCreateData replyData = returnTypeConverter.convert(commandMethod.getReturnType(), MessageCreateData.class);
                if (null != replyData) {
                    event.reply(replyData).queue();
                }
            }
        } catch (InvocationTargetException | IllegalAccessException ex) {
            throw new IllegalStateException("Failed to invoke command method", ex);
        }
    }

    private Injectables createContext(SlashCommandInteractionEvent event) {
        Injectables.Builder context = Injectables.builder()
            .add(JDA.class, event::getJDA)
            .add(SlashCommandInteractionEvent.class, () -> event)
            .add(MessageChannel.class, event::getChannel);

        if (event.getGuild() != null) {
            context.add(Guild.class, event::getGuild)
                .add(GuildId.class, () -> new GuildId(event.getGuild().getId()));
        }

        return context.build();
    }

    @SuppressWarnings("unchecked")
    private <T> T createCommandInstance(SlashCommandInteractionEvent event, Class<T> commandClass) {
        // Locate command class constructor
        Constructor<T> commandConstructor = (Constructor<T>) Arrays.stream(commandClass.getConstructors())
            .filter(constructor -> null != constructor.getAnnotation(SlashCommandCreator.class))
            .findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("Command type '%s' has no constructor annotated with @SlashCommandCreator", commandClass.getSimpleName())));
        Object[] parameters = Arrays.stream(commandConstructor.getParameters())
            .map(parameter -> {
                SlashCommandOption option = parameter.getAnnotation(SlashCommandOption.class);
                if (null == option) {
                    logger.debug("Unknown mapping for command creator parameter type '{}'", parameter.getType().getSimpleName());
                    return null;
                }
                return event.getOption(option.name(), optionTypeConverter.get(OptionMapping.class, parameter.getType()).function());
            }).toArray();

        try {
            return commandConstructor.newInstance(parameters);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new IllegalArgumentException(String.format("Unable to construct instances of command '%s'", commandClass.getSimpleName()), ex);
        }
    }

}
