package com.tavern.utilities.convert;

record ConversionKey<From, To>(Class<From> from, Class<To> to) {
    @Override
    public String toString() {
        return "ConversionKey{" + from.getSimpleName() + " -> " + to.getSimpleName() + '}';
    }
}
