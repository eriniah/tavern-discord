package com.tavern.domain.model.repository;

import com.tavern.domain.model.IdentifiedDomainObject;
import com.tavern.domain.model.Identifier;

import java.util.Optional;

/**
 * Repository for retrieving a single object
 * @param <Id> The identifier type for the object
 * @param <IDO> The object type that is retrieved
 */
public interface GetOneRepository<Id extends Identifier, IDO extends IdentifiedDomainObject<Id>> {
    /**
     * Retrieve an object based on its identifier
     * @param id The identifier of the object to retrieve
     * @return The object retrieved, empty if not found
     */
    Optional<IDO> get(Id id);
    /**
     * Check if an object exists based on its identifier
     * @param id The identifier of the object to check
     * @return True if the object exists, false otherwise
     */
    default boolean exists(Id id) {
        return get(id).isPresent();
    }
}
