package com.tavern.discord.layer.annotations;

import java.lang.annotation.*;

/**
 * Injectables provided to the Framework
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
}
