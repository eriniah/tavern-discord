package com.tavern.utilities.convert;

import java.util.Objects;

public final class ToStringTypeConverterProvider implements TypeConverterProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <From, To> TypeConverter<From, To> get(TypeConverterRegistry registry, Class<From> from, Class<To> to) {
        if (String.class.equals(to)) {
            return TypeConverter.of(from, to, f -> (To) Objects.toString(f));
        }
        return null;
    }
}
