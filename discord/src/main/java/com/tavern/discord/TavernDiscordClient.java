package com.tavern.discord;

import com.tavern.discord.layer.command.slash.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.util.Arrays;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

public final class TavernDiscordClient implements AutoCloseable {
    private static final XLogger logger = XLoggerFactory.getXLogger(TavernDiscordClient.class);

    private final JDA jda;
    private final String defaultCommandPrefix;

    public TavernDiscordClient(String token, String defaultCommandPrefix) {
        jda = JDABuilder
            .create(token, Arrays.asList(
                SCHEDULED_EVENTS,
                MESSAGE_CONTENT,
                GUILD_MESSAGE_REACTIONS,
                GUILD_MESSAGES,
                GUILD_PRESENCES,
                GUILD_VOICE_STATES,
                GUILD_MEMBERS
            ))
            .disableCache(CacheFlag.EMOJI, CacheFlag.STICKER)
            .addEventListeners()
            .build();
        jda.updateCommands().addCommands(new SlashCommandListener() {}.getCommand()).queue();
        this.defaultCommandPrefix = defaultCommandPrefix;
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
}
