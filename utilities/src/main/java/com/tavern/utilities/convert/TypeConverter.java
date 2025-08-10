package com.tavern.utilities.convert;

import java.util.function.Function;

public interface TypeConverter<From, To> {
    To convert(From from);
    Class<From> getFrom();
    Class<To> getTo();

    static <From, To> TypeConverter<From, To> of(Class<From> from, Class<To> to, Function<? super From, To> converter) {
        return new TypeConverterSupport<From, To>(from, to) {
            @Override
            public To convert(From from) {
                return converter.apply(from);
            }
        };
    }
}
