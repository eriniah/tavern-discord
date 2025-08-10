package com.tavern.utilities.convert;

public sealed interface TypeConverterRegistry permits TypeConverterProviderRegistry, TypeConverterRegistryImpl, TypeConverterRegistryRegistry {
    <From, To> TypeConverter<From, To> get(Class<From> from, Class<To> to);

    @SuppressWarnings("unchecked")
    default <From, To> To convert(From from, Class<To> to) {
        return get(((Class<From>) from.getClass()), to).convert(from);
    }
}
