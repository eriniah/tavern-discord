package com.tavern.discord.layer.annotations;

import java.lang.annotation.*;

/**
 * Optional contextual injectables provided by a provider during a request
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Context {
}
