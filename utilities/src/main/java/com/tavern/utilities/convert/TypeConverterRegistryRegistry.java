package com.tavern.utilities.convert;

import com.tavern.utilities.CollectionUtils;

import java.util.*;

public final class TypeConverterRegistryRegistry implements TypeConverterRegistry, TypeConverterRegistryParentAware {
    private final List<TypeConverterRegistry> registries;

    public TypeConverterRegistryRegistry(List<TypeConverterRegistry> registries) {
        this.registries = CollectionUtils.wrapIfPresent(registries, ArrayList::new);
        update(this);
    }

    @Override
    public <From, To> TypeConverter<From, To> get(Class<From> from, Class<To> to) {
        for (TypeConverterRegistry r: this.registries) {
            TypeConverter<From, To> converter = r.get(from, to);
            if (null != converter) {
                return converter;
            }
        }
        return null;
    }

    @Override
    public void update(TypeConverterRegistry registry) {
        this.registries.forEach(child -> {
            if (child instanceof TypeConverterRegistryParentAware r) {
                r.update(registry);
            }
        });
    }
}
