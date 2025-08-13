package com.tavern.discord.listeners.dice.roll;

import java.util.List;

public interface DiceExpressionValueFormatter {
    String formatDice(int count, int sides);
    String formatConstant(int value);
    String formatRoll(int count, int sides, List<Integer> values);

    static DiceExpressionValueFormatter getDefault() {
        return new DiceExpressionValueFormatter() {
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
                    sb.append(values.get(i));
                }
                return sb.append(")").toString();
            }
        };
    }
}
