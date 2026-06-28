package com.tavern.discord.layer;

import com.tavern.discord.layer.annotations.Inject;
import com.tavern.domain.model.discord.DiscordInterface;
import com.tavern.domain.model.guild.config.GuildConfigRepository;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordInitializationListenerAdapter extends ListenerAdapter {

    @Inject
    private GuildConfigRepository guildConfigRepository;

    @Inject
    private DiscordInterface discordInterface;

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        // TODO: Create new guild object
        // guildConfigRepository.save()
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        // TODO: Remove guild object? Or keep for future so no data loss?
        super.onGuildLeave(event);
    }
}
