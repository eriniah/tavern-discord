package com.tavern.utilities.convert;

import java.util.Objects;

/**
 * Handles conversions between Numbers, and between Numbers and Strings
 */
class NumberTypeConverterProvider implements TypeConverterProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <From, To> TypeConverter<From, To> get(TypeConverterRegistry registry, Class<From> from, Class<To> to) {
        if (Number.class.isAssignableFrom(from) && Number.class.isAssignableFrom(to)) {
            return new TypeConverterSupport<>(from, to) {
                @Override
                public To convert(From pFrom) {
                    Number from = (Number) pFrom;
                    if (getTo().equals(Long.class)) {
                        return (To) Long.valueOf(from.longValue());
                    } else if (getTo().equals(Integer.class)) {
                        return (To) Integer.valueOf(from.intValue());
                    } else if (getTo().equals(Short.class)) {
                        return (To) Short.valueOf(from.shortValue());
                    } else if (getTo().equals(Byte.class)) {
                        return (To) Byte.valueOf(from.byteValue());
                    } else if (getTo().equals(Double.class)) {
                        return (To) Double.valueOf(from.doubleValue());
                    } else if (getTo().equals(Float.class)) {
                        return (To) Float.valueOf(from.floatValue());
                    }
                    throw new IllegalStateException(String.format("Unhandled conversion from %s to %s", getFrom(), getTo()));
                }
            };
        } else if (Number.class.isAssignableFrom(from) && String.class.isAssignableFrom(to)) {
            return TypeConverter.of(from, to, f -> (To) Objects.toString(f));
        } else if (String.class.isAssignableFrom(from) && Number.class.isAssignableFrom(to)) {
            return new TypeConverterSupport<>(from, to) {
                @Override
                public To convert(From pFrom) {
                    String from = (String) pFrom;
                    if (getTo().equals(Long.class)) {
                        return (To) Long.valueOf(from);
                    } else if (getTo().equals(Integer.class)) {
                        return (To) Integer.valueOf(from);
                    } else if (getTo().equals(Short.class)) {
                        return (To) Short.valueOf(from);
                    } else if (getTo().equals(Byte.class)) {
                        return (To) Byte.valueOf(from);
                    } else if (getTo().equals(Double.class)) {
                        return (To) Double.valueOf(from);
                    } else if (getTo().equals(Float.class)) {
                        return (To) Float.valueOf(from);
                    }
                    throw new IllegalStateException(String.format("Unhandled conversion from %s to %s", getFrom(), getTo()));
                }
            };
        }
        return null;
    }
}
