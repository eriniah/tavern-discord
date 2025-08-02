package com.tavern.discord.layer.command.slash.annotations;

public @interface SlashCommandHandler {
    /**
     * @return Command data class annotated with @Command
     */
    Class<?> value() default Void.class;
}
