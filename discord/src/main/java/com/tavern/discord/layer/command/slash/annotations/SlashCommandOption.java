package com.tavern.discord.layer.command.slash.annotations;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommandOption {
    // TODO: EMM Think this can just be auto based on property type
    // OptionType type();
    String name();
    String description();
    boolean required() default false;
    boolean autoComplete() default false;
}
