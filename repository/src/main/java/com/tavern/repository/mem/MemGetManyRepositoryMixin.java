package com.tavern.repository.mem;

import com.tavern.domain.model.IdentifiedDomainObject;
import com.tavern.domain.model.Identifier;
import com.tavern.domain.model.repository.*;

public interface MemGetManyRepositoryMixin<Id extends Identifier, IDO extends IdentifiedDomainObject<Id>> extends GetManyRepository<IDO> {
    MemDataRepositoryCache<Id, IDO> getCache();

    @Override
    default RepositoryIterable<IDO> getMany(GetOptions options) {
        return getCache().getMany(options);
    }
}

