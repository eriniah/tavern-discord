package com.tavern.utilities.convert;

import java.util.Arrays;
import java.util.List;

public final class TypeConverterRegistries {
    private TypeConverterRegistries() {}

    private static final TypeConverterRegistry DEFAULT_TYPES = ofRegistries(
        ofProviders(new NumberTypeConverterProvider(), new ToStringTypeConverterProvider())
    );

    public static TypeConverterRegistry defaultRegistry() {
        return DEFAULT_TYPES;
    }

    public static TypeConverterRegistry ofConverters(TypeConverter<?, ?> ...converters) {
        return ofConverters(Arrays.asList(converters));
    }

    public static TypeConverterRegistry ofConverters(List<TypeConverter<?, ?>> converters) {
        return new TypeConverterRegistryImpl(converters);
    }

    public static TypeConverterRegistryBuilder ofConvertersBuilder() {
        return new TypeConverterRegistryBuilder();
    }

    public static TypeConverterRegistry ofProviders(TypeConverterProvider ...providers) {
        return ofProviders(Arrays.asList(providers));
    }

    public static TypeConverterRegistry ofProviders(List<TypeConverterProvider> providers) {
        return new TypeConverterProviderRegistry(providers);
    }

    public static TypeConverterRegistry ofRegistries(TypeConverterRegistry ...registries) {
        return ofRegistries(Arrays.asList(registries));
    }

    public static TypeConverterRegistry ofRegistries(List<TypeConverterRegistry> registries) {
        return new TypeConverterRegistryRegistry(registries);
    }
}
