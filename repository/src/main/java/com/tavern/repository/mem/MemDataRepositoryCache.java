package com.tavern.repository.mem;

import com.tavern.domain.model.IdentifiedDomainObject;
import com.tavern.domain.model.Identifier;
import com.tavern.domain.model.repository.GetOptions;

import java.util.*;

public final class MemDataRepositoryCache<Id extends Identifier, IDO extends IdentifiedDomainObject<Id>> {

    private final Map<Id, IDO> idoMap = new HashMap<>();

    public MemRepositoryIterable<Id, IDO> getMany(GetOptions options) {
        return new MemRepositoryIterable<>(options, this.idoMap.values());
    }

    public Optional<IDO> get(Id id) {
        return Optional.ofNullable(this.idoMap.get(id));
    }

    public void put(IDO ido) {
        this.idoMap.put(ido.getId(), ido);
    }

    public void remove(Id id) {
        this.idoMap.remove(id);
    }

    public void clear() {
        this.idoMap.clear();
    }

}
