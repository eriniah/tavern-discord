package com.tavern.discord.layer.command.slash.annotations;

import com.tavern.discord.layer.annotations.ErrorResponse;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommandHandler {
    /**
     * @return Command data class annotated with @Command
     */
    Class<?> value() default Void.class;
    /**
     * @return Return error messages of these types to the user
     */
    ErrorResponse[] errorResponses() default {};
}
