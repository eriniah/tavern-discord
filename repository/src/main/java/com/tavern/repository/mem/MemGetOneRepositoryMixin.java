package com.tavern.repository.mem;

import com.tavern.domain.model.IdentifiedDomainObject;
import com.tavern.domain.model.Identifier;
import com.tavern.domain.model.repository.GetOneRepository;

import java.util.Optional;

public interface MemGetOneRepositoryMixin<Id extends Identifier, IDO extends IdentifiedDomainObject<Id>> extends GetOneRepository<Id, IDO> {
    MemDataRepositoryCache<Id, IDO> getCache();

    @Override
    default Optional<IDO> get(Id id) {
        return getCache().get(id);
    }

}
