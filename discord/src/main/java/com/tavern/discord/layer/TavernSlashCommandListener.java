package com.tavern.discord.layer;

import com.tavern.discord.layer.command.slash.*;
import com.tavern.utilities.CollectionUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

class TavernSlashCommandListener extends ListenerAdapter {
    private final Map<TavernSlashCommand, SlashCommandListener> slashCommandListeners;

    public TavernSlashCommandListener(Map<TavernSlashCommand, SlashCommandListener> slashCommandListeners) {
        this.slashCommandListeners = CollectionUtils.wrapIfPresent(slashCommandListeners, HashMap::new);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (null == event.getGuild()) {
            // Ignore attempted non-guild interactions
            return;
        }

        // TODO: EMM
        //       Maybe refactor map to be comand id/name -> Pair<Command, Listener> ?
        //       Do magical lookups
        //       Accept the injectables and attempt to populate
        //       Instantiate command
        //       Listeners aren't one per request. Only except @Context as parameter or via a ContextResolver Injectable
    }

}
