package com.tavern.domain.model.repository;

import java.util.*;
import java.util.function.Supplier;

/**
 * Iterable with extended functionality for Repository queries
 * @param <T> The item type
 */
public interface RepositoryIterable<T> extends Iterable<T> {
    /**
     * Retrieve the first item in the iterable, if any
     * @return The first item, empty if there are no items
     */
    Optional<T> first();
    /**
     * Put the items in this iterable into a collection
     * @param collectionSupplier Supplier for the collection
     * @return The collection with the items added
     * @param <C> The collection type
     */
    <C extends Collection<T>> C into(Supplier<C> collectionSupplier);
    /**
     * Put the items in this iterable into a collection
     * @param collection The collection
     * @return The collection with the items added
     * @param <C> The collection type
     */
    <C extends Collection<T>> C into(C collection);
}
