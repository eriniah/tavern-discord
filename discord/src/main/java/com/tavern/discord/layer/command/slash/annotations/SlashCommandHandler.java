package com.tavern.discord.layer.command.slash.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommandHandler {
    /**
     * @return Command data class annotated with @Command
     */
    Class<?> value() default Void.class;
}
