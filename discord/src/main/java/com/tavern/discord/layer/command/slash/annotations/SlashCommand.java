package com.tavern.discord.layer.command.slash.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommand {
    String name();
    String description();
}
