package com.tavern.discord.listeners.dice;

import com.tavern.discord.layer.annotations.*;
import com.tavern.discord.layer.command.slash.*;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandHandler;
import com.tavern.discord.listeners.dice.roll.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.time.Instant;
import java.util.Random;

import static com.tavern.discord.DiscordColoredTextFactory.Ansi.ansiWrap;
import static com.tavern.discord.DiscordColoredTextFactory.Ansi.lightBlue;

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

    @SlashCommandHandler(errorResponses = {
        @ErrorResponse(IllegalArgumentException.class)
    })
    public MessageCreateData handle(DiceRollCommand command) {
        DiceExpression expression = diceFactory.dice(command.getCount(), command.getSides());
        DiceExpression rolled = expression.roll(RANDOM);
        int total = rolled.evaluate(RANDOM);
        return createMessage(expression, rolled, total);
    }

    @SlashCommandHandler(errorResponses = {
        @ErrorResponse(IllegalArgumentException.class)
    })
    public void handle(ExpressionRollCommand command) {
        DiceExpression expression = diceFactory.parseExpression(command.getExpression());
        DiceExpression rolled = expression.roll(RANDOM);
        int total = rolled.evaluate(RANDOM);
        event.reply(createMessage(expression, rolled, total))
            .setEphemeral(command.isHidden())
            .queue();
    }

    private MessageCreateData createMessage(DiceExpression expression, DiceExpression rolled, int total) {
        DiceExpressionValueFormatter formatter = new DiscordDiceValueFormatter();
        return MessageCreateData.fromContent(
        """
        > %s
        %s
        """.formatted(expression.getRepresentation(formatter), ansiWrap("%s = %s".formatted(rolled.getRepresentation(formatter), lightBlue("" + total)))));
    }

}
