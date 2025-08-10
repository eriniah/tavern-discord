package com.tavern.utilities.convert;

import com.tavern.utilities.CollectionUtils;

import java.util.*;

final class TypeConverterProviderRegistry implements TypeConverterRegistry, TypeConverterRegistryParentAware {
    private final List<TypeConverterProvider> providers;
    private TypeConverterRegistry parentRegistry;

    TypeConverterProviderRegistry(List<TypeConverterProvider> providers) {
        this.providers = CollectionUtils.wrapIfPresent(providers, ArrayList::new);
        update(this);
    }

    @Override
    public <From, To> TypeConverter<From, To> get(Class<From> from, Class<To> to) {
        for (TypeConverterProvider provider: providers) {
            TypeConverter<From, To> converter = provider.get(parentRegistry, from, to);
            if (null != converter) {
                return converter;
            }
        }
        return null;
    }

    @Override
    public void update(TypeConverterRegistry registry) {
        this.parentRegistry = registry;
    }
}
