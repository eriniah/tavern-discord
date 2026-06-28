package com.tavern.repository.mem;

import com.tavern.domain.model.IdentifiedDomainObject;
import com.tavern.domain.model.Identifier;
import com.tavern.domain.model.repository.SaveRepository;

public interface MemSaveRepositoryMixin<Id extends Identifier, IDO extends IdentifiedDomainObject<Id>> extends SaveRepository<Id, IDO> {
    MemDataRepositoryCache<Id, IDO> getCache();

    @Override
    default Id save(IDO ido) {
        if (ido.getId() == null) {
            ido.setId(newId());
        }
        getCache().put(ido);
        return ido.getId();
    }
}
