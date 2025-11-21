package com.tavern.domain.model.repository;

import com.tavern.domain.model.IdentifiedDomainObject;

/**
 * Repository for retrieving multiple objects
 * @param <IDO> The type of object to retrieve
 */
public interface GetManyRepository<IDO extends IdentifiedDomainObject<?>> {
    /**
     * Retrieve a list of objects based on the options provided
     * @param options The options to use for retrieving the objects
     * @return The list of objects retrieved
     */
    RepositoryIterable<IDO> getMany(GetOptions options);
}
