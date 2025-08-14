package com.tavern.discord.layer;

import com.tavern.discord.layer.command.slash.SlashCommandListener;
import com.tavern.discord.layer.command.slash.TavernSlashCommandFactory;
import com.tavern.utilities.convert.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.util.*;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

public final class TavernDiscordClient implements AutoCloseable {
    private static final XLogger logger = XLoggerFactory.getXLogger(TavernDiscordClient.class);

    private final JDA jda;
    private final String defaultCommandPrefix;
    private final Injectables injectables;
    private final TavernSlashCommandCache slashCommandCache;
    private final TypeConverterRegistry typeConverter;

    private TavernDiscordClient(TavernDiscordClient.Builder builder) {
        this.defaultCommandPrefix = builder.defaultCommandPrefix;
        this.injectables = Objects.requireNonNullElseGet(builder.injectables, () -> new Injectables.Builder().build());

        // Internal type conversions
        TypeConverterRegistry typeConverter = TypeConverterRegistries.ofRegistries(
            TypeConverterRegistries.ofConvertersBuilder()
                .add(TypeConverter.of(OptionMapping.class, String.class, OptionMapping::getAsString))
                .add(TypeConverter.of(OptionMapping.class, Long.class, OptionMapping::getAsLong))
                .add(TypeConverter.of(OptionMapping.class, Long.TYPE, OptionMapping::getAsLong))
                .add(TypeConverter.of(OptionMapping.class, Integer.class, OptionMapping::getAsInt))
                .add(TypeConverter.of(OptionMapping.class, Integer.TYPE, OptionMapping::getAsInt))
                .add(TypeConverter.of(OptionMapping.class, Double.class, OptionMapping::getAsDouble))
                .add(TypeConverter.of(OptionMapping.class, Double.TYPE, OptionMapping::getAsDouble))
                .add(TypeConverter.of(OptionMapping.class, Boolean.class, OptionMapping::getAsBoolean))
                .add(TypeConverter.of(OptionMapping.class, Boolean.TYPE, OptionMapping::getAsBoolean))
                .add(TypeConverter.of(OptionMapping.class, Message.Attachment.class, OptionMapping::getAsAttachment))
                .add(TypeConverter.of(OptionMapping.class, GuildChannelUnion.class, OptionMapping::getAsChannel))
                .add(TypeConverter.of(OptionMapping.class, Member.class, OptionMapping::getAsMember))
                .add(TypeConverter.of(OptionMapping.class, IMentionable.class, OptionMapping::getAsMentionable))
                .add(TypeConverter.of(OptionMapping.class, Role.class, OptionMapping::getAsRole))
                .add(TypeConverter.of(OptionMapping.class, User.class, OptionMapping::getAsUser))
                .build(),
            TypeConverterRegistries.defaultRegistry()
        );
        if (null != builder.typeConverter) {
            typeConverter = TypeConverterRegistries.ofRegistries(
                builder.typeConverter,
                typeConverter
            );
        }
        this.typeConverter = typeConverter;

        this.slashCommandCache = new TavernSlashCommandCache(new TavernSlashCommandFactory(), builder.slashCommandListeners);

        jda = JDABuilder
            .create(builder.token, Arrays.asList(
                SCHEDULED_EVENTS,
                MESSAGE_CONTENT,
                GUILD_MESSAGE_REACTIONS,
                GUILD_MESSAGES,
                GUILD_PRESENCES,
                GUILD_VOICE_STATES,
                GUILD_MEMBERS
            ))
            .disableCache(CacheFlag.EMOJI, CacheFlag.STICKER)
            .addEventListeners(new SlashCommandListenerAdaptor(injectables, typeConverter, slashCommandCache))
            .build();
    }

    public boolean awaitReady() throws InterruptedException {
        JDA.Status[] failOn = { JDA.Status.FAILED_TO_LOGIN, JDA.Status.SHUTTING_DOWN, JDA.Status.DISCONNECTED };
        jda.awaitStatus(JDA.Status.CONNECTED,
            failOn
        );

        JDA.Status status = jda.getStatus();
        if (JDA.Status.CONNECTED == status) {
            this.slashCommandCache.postCommands(jda);
            return true;
        } else {
            logger.error("Failed to connect to Discord API. Status: {}", status);
            return false;
        }
    }

    @Override
    public void close() {
        jda.shutdown();
    }

    public static Builder builder(String token) {
        return new Builder(token);
    }

    public static class Builder {
        private final String token;
        private final List<Class<? extends SlashCommandListener>> slashCommandListeners = new ArrayList<>();
        private String defaultCommandPrefix = "$";
        private Injectables injectables = null;
        private TypeConverterRegistry typeConverter = null;

        public Builder(String token) {
            this.token = token;
        }

        public Builder commandPrefix(String prefix) {
            this.defaultCommandPrefix = prefix;
            return this;
        }

        public Builder listener(Class<? extends SlashCommandListener> listener) {
            slashCommandListeners.add(listener);
            return this;
        }

        public Builder listeners(List<Class<? extends SlashCommandListener>> listeners) {
            slashCommandListeners.addAll(listeners);
            return this;
        }

        public Builder injectables(Injectables injectables) {
            this.injectables = injectables;
            return this;
        }

        public Builder typeConverter(TypeConverterRegistry typeConverter) {
            this.typeConverter = typeConverter;
            return this;
        }

        public TavernDiscordClient build() {
            return new TavernDiscordClient(this);
        }

    }
}
