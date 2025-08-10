package com.tavern.utilities.convert;

import jakarta.annotation.Nonnull;

public abstract class BiTypeConverterSupport<A, B> implements BiTypeConverter<A, B> {
    private final Class<A> aType;
    private final Class<B> bType;

    protected BiTypeConverterSupport(@Nonnull Class<A> aType, @Nonnull Class<B> bType) {
        this.aType = aType;
        this.bType = bType;
    }

    public abstract B convertA(A a);
    public abstract A convertB(B b);

    @Override
    public TypeConverter<A, B> aToB() {
        return new TypeConverterSupport<>(aType, bType) {
            @Override
            public B convert(A a) {
                return convertA(a);
            }
        };
    }

    @Override
    public TypeConverter<B, A> bToA() {
        return new TypeConverterSupport<>(bType, aType) {
            @Override
            public A convert(B b) {
                return convertB(b);
            }
        };
    }
}
