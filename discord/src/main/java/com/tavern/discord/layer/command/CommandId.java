package com.tavern.discord.layer.command;

import com.tavern.utilities.StringUtils;
import jakarta.annotation.Nullable;

public record CommandId(String command, @Nullable String subCommandGroup, @Nullable String subCommand) {

    public String toString() {
        StringBuilder builder = new StringBuilder()
            .append(command);

        if (!StringUtils.isNullOrBlank(subCommandGroup)) {
            builder.append(":").append(subCommandGroup);
        }

        if (!StringUtils.isNullOrBlank(subCommand)) {
            builder.append(":").append(subCommand);
        }

        return builder.toString();
    }
}
