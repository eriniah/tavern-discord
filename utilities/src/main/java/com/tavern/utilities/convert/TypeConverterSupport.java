package com.tavern.utilities.convert;

import jakarta.annotation.Nonnull;

public abstract class TypeConverterSupport<From, To> implements TypeConverter<From, To> {
    private final Class<From> from;
    private final Class<To> to;

    protected TypeConverterSupport(@Nonnull Class<From> from, @Nonnull Class<To> to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public abstract To convert(From from);

    @Override
    public Class<From> getFrom() {
        return from;
    }

    @Override
    public Class<To> getTo() {
        return to;
    }
}
