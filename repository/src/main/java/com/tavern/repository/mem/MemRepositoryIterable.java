package com.tavern.repository.mem;

import com.google.common.collect.Iterators;
import com.tavern.domain.model.IdentifiedDomainObject;
import com.tavern.domain.model.Identifier;
import com.tavern.domain.model.repository.GetOptions;
import com.tavern.domain.model.repository.RepositoryIterable;

import java.util.*;
import java.util.function.Supplier;

public class MemRepositoryIterable<Id extends Identifier, IDO extends IdentifiedDomainObject<Id>> implements RepositoryIterable<IDO> {
    private final Iterable<IDO> iterable;
    private int skip;
    private int take;

    public MemRepositoryIterable(GetOptions options, Collection<IDO> coll) {
        this.iterable = coll;
        this.skip = Math.max(options.offset(), 0);
        this.take = options.limit() <= 0 ? Integer.MAX_VALUE : options.limit();
        if (!options.orderedSorts().isEmpty()) {
            throw new UnsupportedOperationException("Mem repository doesn't support sorting yet");
        }
    }

    @Override
    public Optional<IDO> first() {
        Iterator<IDO> iter = iterator();
        if (iter.hasNext()) {
            return Optional.of(iter.next());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public <C extends Collection<IDO>> C into(Supplier<C> collectionSupplier) {
        return into(collectionSupplier.get());
    }

    @Override
    public <C extends Collection<IDO>> C into(C collection) {
        Iterators.addAll(collection, iterator());
        return collection;
    }

    @Override
    public Iterator<IDO> iterator() {
        Iterator<IDO> iter = iterable.iterator();
        // Throw out skipped values
        while (iter.hasNext() && 0 < skip) {
            iter.next();
        }
        return Iterators.limit(iter, take);
    }
}
