package com.tavern.utilities.convert;

public interface TypeConverterProvider {
    <From, To> TypeConverter<From, To> get(TypeConverterRegistry registry, Class<From> from, Class<To> to);
}
