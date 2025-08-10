package com.tavern.utilities.convert;

import java.util.*;

public final class TypeConverterRegistryBuilder {
    private final List<TypeConverter<?, ?>> converters = new LinkedList<>();

    TypeConverterRegistryBuilder() {}

    public TypeConverterRegistryBuilder add(TypeConverter<?, ?> converter) {
        converters.add(converter);
        return this;
    }

    public TypeConverterRegistryBuilder add(BiTypeConverter<?, ?> converter) {
        converters.add(converter.aToB());
        converters.add(converter.bToA());
        return this;
    }

    public TypeConverterRegistry build() {
        return new TypeConverterRegistryImpl(converters);
    }

}
