package com.tavern.utilities;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * A immutable, non nullable implementation of Pair
 * @param <L> Left value type
 * @param <R> Right value type
 */
public final class Pair<L, R> {
    private final L left;
    private final R right;

    public Pair(L left, R right) throws IllegalArgumentException {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Pair values cannot be null");
        }

        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "Pair{" + left +
            ", " + right +
            '}';
    }

    @Nonnull
    public L getLeft() {
        return left;
    }

    @Nonnull
    public R getRight() {
        return right;
    }

}
