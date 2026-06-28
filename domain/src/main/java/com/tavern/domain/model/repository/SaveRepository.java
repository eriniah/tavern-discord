package com.tavern.domain.model.repository;

import com.tavern.domain.model.IdentifiedDomainObject;
import com.tavern.domain.model.Identifier;

/**
 * Repository for saving objects
 * @param <Id> The identifier type for the object
 * @param <IDO> The object type that is stored
 */
public interface SaveRepository<Id extends Identifier, IDO extends IdentifiedDomainObject<Id>> {
    /**
     * Create a new identifier for a new object.
     * @return The new identifier
     * @throws UnsupportedOperationException If the repository does not support creating new identifiers
     */
    Id newId();
    /**
     * Save an object to the repository.
     * Creates a new object or updates an existing object based on the identifier.
     * @param ido The object to save
     * @return The identifier of the saved object
     */
    Id save(IDO ido);
}
