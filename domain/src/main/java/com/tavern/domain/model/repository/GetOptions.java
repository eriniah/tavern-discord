package com.tavern.domain.model.repository;

import java.util.Collections;
import java.util.List;

public record GetOptions(int offset, int limit, List<Sort> orderedSorts) {

    public GetOptions() {
        this(0, 0, Collections.emptyList());
    }

}
