package com.tavern.discord.listeners.dice;

import com.tavern.discord.layer.annotations.Inject;
import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandHandler;
import com.tavern.discord.listeners.dice.roll.DiceFactory;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class RollSlashCommandListener implements SlashCommandListener {
    private static final XLogger logger = XLoggerFactory.getXLogger(RollSlashCommandListener.class);

    @Inject
    private DiceFactory diceFactory;

    @Override
    public TavernSlashCommand getCommand(TavernSlashCommandFactory factory) {
        return factory.builder("roll", "Roll a dice")
            .addSubCommand(DiceRollCommand.class)
            .addSubCommand(ExpressionRollCommand.class)
            .build();
    }

    @SlashCommandHandler
    public void handle(DiceRollCommand command) {
        // TODO: EMM

        // Return MessageCreateData (use builder) and have layer handle mapping
        // Layer should also handle string or string like types getting mapped to content
    }

    @SlashCommandHandler
    public void handle(ExpressionRollCommand command) {
    }

}
