package com.tavern.utilities.convert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

final class TypeConverterRegistryImpl implements TypeConverterRegistry {
    private final Map<ConversionKey<?, ?>, TypeConverter<?, ?>> converters;

    TypeConverterRegistryImpl(List<TypeConverter<?, ?>> converters) {
        this.converters = converters.stream().collect(Collectors.toMap(
            converter -> new ConversionKey<>(converter.getFrom(), converter.getTo()),
            Function.identity(),
            // Prefer first
            (a, b) -> a
        ));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <From, To> TypeConverter<From, To> get(Class<From> from, Class<To> to) throws UnsupportedOperationException {
        ConversionKey<From, To> key = new ConversionKey<>(from, to);

        if (!converters.containsKey(key)) {
            throw new UnsupportedOperationException("No converter found for " + key);
        }
        return (TypeConverter<From, To>) converters.get(key);
    }

}
