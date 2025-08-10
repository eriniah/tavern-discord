package com.tavern.utilities.convert;

import java.util.function.Function;

public interface BiTypeConverter<A, B> {
    TypeConverter<A, B> aToB();
    TypeConverter<B, A> bToA();

    static <A, B> BiTypeConverter<A, B> of(Class<A> aClass, Class<B> bClass, Function<? super A, B> convert, Function<? super B, A> revert) {
        return new BiTypeConverterSupport<A, B>(aClass, bClass) {
            @Override
            public B convertA(A a) {
                return convert.apply(a);
            }

            @Override
            public A convertB(B b) {
                return revert.apply(b);
            }
        };
    }
}
