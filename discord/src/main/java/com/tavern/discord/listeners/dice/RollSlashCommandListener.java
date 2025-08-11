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
        DiceExpression expression = diceFactory.dice(command.getCount(), command.getSides());
        event.reply(String.format("""
        %s = %d
        """, expression.getRepresentation(), expression.evaluate(RANDOM)
        )).queue();
    }

    @SlashCommandHandler
    public String handle(ExpressionRollCommand command) {
        DiceExpression expression = diceFactory.parseExpression(command.getExpression());
        DiceExpression rolled = expression.roll(RANDOM);
        int total = rolled.evaluate(RANDOM);
        return """
        Expression: %s
        Rolled: %s
        Total: %d
        """.formatted(expression, rolled, total);
    }

}
