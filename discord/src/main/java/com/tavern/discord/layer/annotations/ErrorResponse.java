package com.tavern.discord.layer.annotations;

public @interface ErrorResponse {
    /**
     * Return the error message of this type to the user
     */
    Class<? extends Throwable> value();
    /**
     * Defaults to the exception message
     */
    String message() default "";
}
