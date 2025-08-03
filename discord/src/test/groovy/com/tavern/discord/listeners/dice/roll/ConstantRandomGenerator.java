package com.tavern.discord.listeners.dice.roll;

import java.util.random.RandomGenerator;

public final class ConstantRandomGenerator implements RandomGenerator {
    private final int value;

    public ConstantRandomGenerator(int value) {
        this.value = value;
    }

    @Override
    public long nextLong() {
        return value;
    }

    @Override
    public int nextInt(int origin, int bound) {
        return value;
    }

    @Override
    public int nextInt(int bound) {
        return value;
    }

    @Override
    public int nextInt() {
        return value;
    }
}
