package com.tavern.discord.layer.command.slash.annotations;

import java.lang.annotation.*;

@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommandCreator {
}
