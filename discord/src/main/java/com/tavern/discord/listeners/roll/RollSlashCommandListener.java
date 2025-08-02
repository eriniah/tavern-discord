package com.tavern.discord.listeners.roll;

import com.tavern.discord.layer.command.slash.SlashCommandListener;
import com.tavern.discord.layer.command.slash.annotations.SlashCommandHandler;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;

public class RollSlashCommandListener implements SlashCommandListener {
    @Override
    public SlashCommandData getCommand() {
        return Commands.slash("roll", "Roll a dice. Defaults to 1d6")
            .addSubcommands(
                new SubcommandData("dice", "Roll a configured dice")
                    .addOption(OptionType.INTEGER, "sides", "The number of sides on the dice", false)
                    .addOption(OptionType.INTEGER, "dice", "The number of dice to roll", false),
                new SubcommandData("expression", "Custom roll expression similar to roll20")
                    .addOption(OptionType.STRING, "expression", "Format 'NdX [[{+|-} {NdX|M}]...]' where: N = number of dice, X = sides of the dice and M = integer", true)
            );
    }

    @SlashCommandHandler
    public void handle(DiceRollCommand command) {

    }

    @SlashCommandHandler
    public void handle(ExpressionRollCommand command) {
    }
}
