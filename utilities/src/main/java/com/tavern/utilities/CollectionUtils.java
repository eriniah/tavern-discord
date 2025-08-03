package com.tavern.utilities;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;
import java.util.function.Supplier;

public final class CollectionUtils {

    /**
     * Get the first element from a collection
     * @param collection The collection to get the first element from
     * @return The first element from the collection, empty() if there is no element
     * @param <T> The collection element type
     */
    public static <T> Optional<T> first(Collection<T> collection) {
        if (null == collection || collection.isEmpty()) {
            return Optional.empty();
        }

        return collection.stream().findFirst();
    }

    /**
     * Get the last element from a List
     * @param list The list to get the last element from
     * @return The last element from the collection, empty() if there are no elements
     * @param <T> The collection element type
     */
    public static <T> Optional<T> last(List<T> list) {
        if (null == list || list.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(list.get(list.size() - 1));
    }

    /**
     * Remove and return the first element in the List
     * @param list The list to pop() from
     * @return The first element in the List
     * @param <T> The list item type
     * @throws NoSuchElementException If the list was null or empty
     */
    public static <T> T pop(List<T> list)
        throws NoSuchElementException {
        if (null == list || list.isEmpty()) {
            throw new NoSuchElementException("There is no element to pop");
        }

        T t = list.get(0);
        list.remove(0);
        return t;
    }

    /**
     * Wraps the given collection with a new collection obtained from the provided supplier.
     * If the given collection is not null, its contents are added to the new collection.
     *
     * @param collection The collection to wrap, can be null.
     * @param supplier A supplier that provides an instance of the new collection.
     * @param <I> The collection item type
     * @param <C> The Collection type
     * @return A new collection wrapping the given collection's contents or an empty collection if the input was null.
     */
    @Nonnull
    public static <I, C extends Collection<I>> C wrapIfPresent(@Nullable C collection, @Nonnull Supplier<C> supplier) {
        C wrapper = supplier.get();
        if (null != collection) {
            wrapper.addAll(collection);
        }
        return wrapper;
    }

    /**
     * Wraps the given map with a new map obtained from the provided supplier.
     * If the given map is not null, its contents are added to the new map.
     *
     * @param map The map to wrap, can be null.
     * @param supplier A supplier that provides an instance of the new map.
     * @param <K> The type of keys in the map.
     * @param <V> The type of values in the map.
     * @return A new map wrapping the given map's contents or an empty map if the input was null.
     */
    @Nonnull
    public static <K, V> Map<K, V> wrapIfPresent(@Nullable Map<K, V> map, @Nonnull Supplier<Map<K, V>> supplier) {
        Map<K, V> wrapper = supplier.get();
        if (null != map) {
            wrapper.putAll(map);
        }
        return wrapper;
    }

    public static <V> Stack<V> copy(Stack<V> stack) {
        Stack<V> copy = new Stack<>();
        stack.forEach(copy::push);
        return copy;
    }

    private CollectionUtils() {}
}
