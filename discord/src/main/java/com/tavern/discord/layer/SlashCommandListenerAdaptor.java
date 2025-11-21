package com.tavern.discord.layer;

import com.tavern.discord.layer.annotations.*;
import com.tavern.discord.layer.command.CommandId;
import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.*;
import com.tavern.domain.model.discord.guild.GuildId;
import com.tavern.utilities.StringUtils;
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

        if (!command.isValid(commandId)) {
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

        final Class<?> commandClass = command.getCommandClass(commandId);
        final Object commandInstance = null != commandClass ? createCommandInstance(event, commandClass) : null;

        // First look for command specifiers, then matching command class
        Method commandMethod = Arrays.stream(listener.getClass().getMethods())
            .filter(method -> {
                SlashCommandHandler handler = method.getAnnotation(SlashCommandHandler.class);
                return null != handler
                    && commandId.equals(new CommandId(
                    handler.command(),
                    handler.subCommandGroup().isBlank() ? null : handler.subCommandGroup(),
                    handler.subCommand().isBlank() ? null : handler.subCommand()
                ));
            }).findFirst().orElse(null);
        // If still null, attempt to match by command class
        if (null == commandMethod) {
            commandMethod = Arrays.stream(listener.getClass().getMethods())
                .filter(method -> {
                    SlashCommandHandler handler = method.getAnnotation(SlashCommandHandler.class);
                    return null != handler
                        && Arrays.asList(method.getParameterTypes()).contains(commandClass);
                }).findFirst().orElseThrow(() -> new IllegalStateException("Failed to locate command method for " + commandId));
        }
        SlashCommandHandler commandHandler = commandMethod.getAnnotation(SlashCommandHandler.class);

        // Build method parameters
        Object[] commandParameters = Arrays.stream(commandMethod.getParameters())
            .map(parameter -> {
                Inject inject = parameter.getAnnotation(Inject.class);
                Context context = parameter.getAnnotation(Context.class);

                if (null != inject) {
                    return injectables.get(parameter.getType());
                } else if (null != context) {
                    return contextuals.get(parameter.getType());
                } else if (Objects.equals(commandClass, parameter.getType())) {
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
                TypeConverter<?, MessageCreateData> converter = returnTypeConverter.get(commandMethod.getReturnType(), MessageCreateData.class);
                if (null != converter) {
                    MessageCreateData replyData = unsafeConvert(converter, ret);
                    if (null != replyData) {
                        event.reply(replyData).queue();
                    }
                }
            }
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Failed to invoke command method", ex);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            for (ErrorResponse errorResponse: commandHandler.errorResponses()) {
                if (errorResponse.value().isAssignableFrom(cause.getClass())) {
                    if (StringUtils.isNullOrBlank(errorResponse.message())) {
                        event.reply(cause.getMessage()).setEphemeral(true).queue();
                    } else {
                        event.reply(errorResponse.message()).setEphemeral(true).queue();
                    }
                    return;
                }
            }
            logger.error("Failed to execute command", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private <F, T> T unsafeConvert(TypeConverter<F, T> converter, Object value) {
        return converter.convert((F) value);
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
