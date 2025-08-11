package com.tavern.discord.listeners.dice;

import com.tavern.discord.layer.annotations.Context;
import com.tavern.discord.layer.annotations.Inject;
import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandHandler;
import com.tavern.discord.listeners.dice.roll.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.time.Instant;
import java.util.Random;

public class RollSlashCommandListener implements SlashCommandListener {
    private static final XLogger logger = XLoggerFactory.getXLogger(RollSlashCommandListener.class);
    private static final Random RANDOM = new Random(Instant.now().toEpochMilli());

    @Inject
    private DiceFactory diceFactory;

    @Context
    private SlashCommandInteractionEvent event;

    @Override
    public TavernSlashCommand getCommand(TavernSlashCommandFactory factory) {
        return factory.builder("roll", "Roll a dice")
            .addSubCommand(DiceRollCommand.class)
            .addSubCommand(ExpressionRollCommand.class)
            .build();
    }

    @SlashCommandHandler
    public void handle(DiceRollCommand command) {
        event.reply(String.format("""
        Rolled %dd%d": %d
        """, command.getCount(), command.getSides(), diceFactory.dice(command.getCount(), command.getSides()).evaluate(RANDOM).getTotal()
        )).queue();
    }

    @SlashCommandHandler
    public void handle(ExpressionRollCommand command) {
        DiceExpression expression = diceFactory.parseExpression(command.getExpression());
        DiceExpressionResult result = expression.evaluate(RANDOM);
        event.reply(String.format("""
        Rolling: %s
        Rolled: %s
        Total: %d
        """, result.getResults()., result.getTotal()));
        event.reply("" + diceFactory.parseExpression(command.getExpression()).evaluate(RANDOM).getTotal())
            .queue();
    }

}
