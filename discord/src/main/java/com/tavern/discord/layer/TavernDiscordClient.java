package com.tavern.discord.layer;

import com.tavern.discord.layer.command.slash.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.util.*;
import java.util.function.Consumer;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

public final class TavernDiscordClient implements AutoCloseable {
    private static final XLogger logger = XLoggerFactory.getXLogger(TavernDiscordClient.class);

    private final JDA jda;
    private final String defaultCommandPrefix;
    private final Injectables injectables;
    private final Map<TavernSlashCommand, SlashCommandListener> slashCommandListeners;

    private TavernDiscordClient(TavernDiscordClient.Builder builder) {
        this.defaultCommandPrefix = builder.defaultCommandPrefix;
        this.injectables = builder.injectables.build();

        this.slashCommandListeners = new HashMap<>();
        TavernSlashCommandFactory slashCommandFactory = new TavernSlashCommandFactory();
        for (SlashCommandListener listener : builder.slashCommandListeners) {
            TavernSlashCommand command = listener.getCommand(slashCommandFactory);
            this.slashCommandListeners.put(command, listener);
        }

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
            .addEventListeners(new TavernSlashCommandListener())
            .build();
    }

    public boolean awaitReady() throws InterruptedException {
        JDA.Status[] failOn = { JDA.Status.FAILED_TO_LOGIN, JDA.Status.SHUTTING_DOWN, JDA.Status.DISCONNECTED };
        jda.awaitStatus(JDA.Status.CONNECTED,
            failOn
        );

        JDA.Status status = jda.getStatus();
        if (JDA.Status.CONNECTED == status) {
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

    public Builder builder(String token) {
        return new Builder(token);
    }

    public static class Builder {
        private final String token;
        private final Injectables.Builder injectables;
        private final List<SlashCommandListener> slashCommandListeners = new ArrayList<>();
        private String defaultCommandPrefix = "$";

        public Builder(String token) {
            this.token = token;
            this.injectables = Injectables.builder();
        }

        public Builder commandPrefix(String prefix) {
            this.defaultCommandPrefix = prefix;
            return this;
        }

        public Builder listener(SlashCommandListener listener) {
            slashCommandListeners.add(listener);
            return this;
        }

        public Builder listeners(SlashCommandListener ...listener) {
            Arrays.stream(listener).forEach(this::listener);
            return this;
        }

        public Builder injectables(Consumer<Injectables.Builder> configurer) {
            configurer.accept(injectables);
            return this;
        }

        public TavernDiscordClient build() {
            return new TavernDiscordClient(this);
        }

    }
}
