package com.tavern.discord.listeners.dice;

import com.tavern.discord.DiscordColoredTextFactory;
import com.tavern.discord.listeners.dice.roll.*;

import java.util.List;

public final class DiscordDiceValueFormatter implements DiceExpressionValueFormatter {

    @Override
    public String formatDice(int count, int sides) {
        return "%dd%d".formatted(count, sides);
    }

    @Override
    public String formatConstant(int value) {
        return Integer.toString(value);
    }

    @Override
    public String formatRoll(int count, int sides, List<Integer> values) {
        StringBuilder sb = new StringBuilder()
            .append("(");
        for (int i = 0; i < values.size(); i++) {
            if (0 < i) {
                sb.append(" + ");
            }

            int v = values.get(i);
            if (v == sides) {
                sb.append(DiscordColoredTextFactory.Ansi.yellowGreen(Integer.toString(v)));
            } else if (v == 1) {
                sb.append(DiscordColoredTextFactory.Ansi.red(Integer.toString(v)));
            } else {
                sb.append(v);
            }
        }
        return sb.append(")").toString();
    }

}
